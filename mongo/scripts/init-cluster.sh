#!/bin/bash
set -euo pipefail

CA=/etc/ssl/mongo/ca.pem
CERT=/etc/ssl/mongo/mongo.pem
KEYFILE=/etc/ssl/mongo/keyfile

MONGO_INITDB_ROOT_USERNAME="${MONGO_INITDB_ROOT_USERNAME:-admin}"
MONGO_INITDB_ROOT_PASSWORD="${MONGO_INITDB_ROOT_PASSWORD:-ChangeMeStrongPassword123!}"

MONGO_CLIENT="mongosh"

wait_for() {
  local hostport="$1"
  echo "Waiting for ${hostport}..."
  until ${MONGO_CLIENT} --tls --tlsCAFile "${CA}" --tlsCertificateKeyFile "${CERT}" --host "${hostport}" --eval "db.adminCommand({ping:1})" > /dev/null 2>&1; do
    sleep 2
  done
  echo "${hostport} is up."
}

# Wait for certs/keyfile
until [ -f "${CERT}" ] && [ -f "${CA}" ] && [ -f "${KEYFILE}" ]; do
  echo "Waiting for certs and keyfile in ${CERT}, ${CA}, ${KEYFILE}..."
  sleep 1
done

# Wait for config & shards & mongos
wait_for "cfg1:27019"
wait_for "cfg2:27019"
wait_for "cfg3:27019"

wait_for "shard1:27018"
wait_for "shard2:27018"
wait_for "shard3:27018"

wait_for "mongos:27017"

# Initiate config server RS (if not already)
echo "Attempting rs.initiate for rs-config on cfg1..."
${MONGO_CLIENT} --tls --tlsCAFile "${CA}" --tlsCertificateKeyFile "${CERT}" --host cfg1:27019 --eval "try { rs.initiate({_id: 'rs-config', configsvr: true, members: [ {_id:0, host:'cfg1:27019'}, {_id:1, host:'cfg2:27019'}, {_id:2, host:'cfg3:27019'} ]}); print('rs-config initiate attempted'); } catch(e) { print(e); }"

# Wait for config primary
echo "Waiting for config server primary..."
until ${MONGO_CLIENT} --tls --tlsCAFile "${CA}" --tlsCertificateKeyFile "${CERT}" --host cfg1:27019 --eval "rs.isMaster().ismaster" | grep -q "true"; do
  sleep 2
done
echo "config server primary ready."

# Initiate shard RS
echo "Attempting rs.initiate for rs-shard on shard1..."
${MONGO_CLIENT} --tls --tlsCAFile "${CA}" --tlsCertificateKeyFile "${CERT}" --host shard1:27018 --eval "try { rs.initiate({_id: 'rs-shard', members: [ {_id:0, host:'shard1:27018'}, {_id:1, host:'shard2:27018'}, {_id:2, host:'shard3:27018'} ]}); print('rs-shard initiate attempted'); } catch(e) { print(e); }"

# Wait for shard primary
echo "Waiting for shard primary..."
until ${MONGO_CLIENT} --tls --tlsCAFile "${CA}" --tlsCertificateKeyFile "${CERT}" --host shard1:27018 --eval "rs.isMaster().ismaster" | grep -q "true"; do
  sleep 2
done
echo "shard primary ready."

# Add shard to cluster via mongos
echo "Adding shard to mongos..."
${MONGO_CLIENT} --tls --tlsCAFile "${CA}" --tlsCertificateKeyFile "${CERT}" --host mongos:27017 --eval "try { sh.addShard('rs-shard/shard1:27018,shard2:27018,shard3:27018'); print('addShard attempted'); } catch(e) { print(e); }"

# Create admin user via mongos (localhost auth bypass allows initial creation)
echo "Creating admin user if not exists..."
CREATE_USER_JS="
var admin = db.getSiblingDB('admin');
var user = admin.getUser('${MONGO_INITDB_ROOT_USERNAME}');
if (user == null) {
  admin.createUser({user: '${MONGO_INITDB_ROOT_USERNAME}', pwd: '${MONGO_INITDB_ROOT_PASSWORD}', roles: [ { role: 'root', db: 'admin' } ]});
  print('Created admin user');
} else {
  print('Admin user already exists');
}
"
${MONGO_CLIENT} --tls --tlsCAFile "${CA}" --tlsCertificateKeyFile "${CERT}" --host mongos:27017 --eval "${CREATE_USER_JS}"

echo "Initialization complete."
