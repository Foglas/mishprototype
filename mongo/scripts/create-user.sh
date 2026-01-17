#!/usr/bin/env bash
set -euo pipefail

# Create a MongoDB user inside the container.
# Usage: ./create-user.sh <username> <password> [role] [roleDb] [authDb]
USER="${1:-appuser}"
PASS="${2:-password}"
ROLE="${3:-readWrite}"
ROLE_DB="${4:-appdb}"
AUTH_DB="${5:-admin}"

CERT_DIR="/etc/ssl/mongo"
MONGO_HOST="localhost"
MONGO_PORT=27017

# Wait for MongoDB to be ready
RETRIES=30
SLEEP=2
echo "Waiting for MongoDB to be available on ${MONGO_HOST}:${MONGO_PORT}..."
for i in $(seq 1 ${RETRIES}); do
  if mongosh --host "${MONGO_HOST}" --port "${MONGO_PORT}" \
      --tls --tlsCertificateKeyFile "${CERT_DIR}/mongo.pem" --tlsCAFile "${CERT_DIR}/ca.pem" \
      --eval 'db.adminCommand("ping")' >/dev/null 2>&1; then
    echo "MongoDB is up!"
    break
  fi
  echo "Waiting... ($i/${RETRIES})"
  sleep ${SLEEP}
done

# Create the user
cat > /tmp/create_user.js <<'JS'
db = db.getSiblingDB(process.env.AUTH_DB || 'admin');
try {
  db.createUser({
    user: process.env.MONGO_INITDB_ROOT_USERNAME || 'appuser',
    pwd: process.env.MONGO_INITDB_ROOT_PASSWORD || 'password',
    roles: [{ role: process.env.ROLE || 'readWrite', db: process.env.ROLE_DB || 'appdb' }]
  });
  print('User created successfully');
} catch (e) {
  if (e.codeName === 'DuplicateKey') {
    print('User already exists');
  } else {
    throw e;
  }
}
JS

AUTH_DB=${AUTH_DB} \
MONGO_INITDB_ROOT_USERNAME=${USER} \
MONGO_INITDB_ROOT_PASSWORD=${PASS} \
ROLE=${ROLE} \
ROLE_DB=${ROLE_DB} \
mongosh --tls --tlsCertificateKeyFile "${CERT_DIR}/mongo.pem" \
       --tlsCAFile "${CERT_DIR}/ca.pem" \
       /tmp/create_user.js

rm -f /tmp/create_user.js
echo "User creation finished."
