#!/usr/bin/env bash
set -euo pipefail

# Create a MongoDB user over TLS.
# Usage: ./create-user.sh <username> <password> [roles] [authDb]
# Example: ./create-user.sh appuser s3cret '[{role:"readWrite",db:"appdb"}]' admin
# If `mongosh` is not installed on the host, the script will try to run it inside
# the Docker container named by MONGO_CONTAINER (default: mongo-tls). You can also
# force Docker use by exporting FORCE_DOCKER=1.

SCRIPTDIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CERT_DIR_HOST="${MONGO_CERT_DIR:-${SCRIPTDIR}/../certs}"
USER="${1:-appuser}"
PASS="${2:-password}"
ROLE="${3:-readWrite}"
ROLE_DB="${4:-appdb}"
AUTH_DB="${5:-admin}"

MONGO_HOST="${MONGO_HOST:-localhost}"
MONGO_PORT="${MONGO_PORT:-27017}"
MONGO_CONTAINER="${MONGO_CONTAINER:-mongo-tls}"
CERT_DIR_CONTAINER="${MONGO_CERT_DIR_IN_CONTAINER:-/etc/ssl/mongo}"

RETRIES=30
SLEEP=2

# Helper to decide whether to use host mongosh or docker exec mongosh
use_docker=0
if [ "${FORCE_DOCKER:-0}" != "0" ]; then
  use_docker=1
elif command -v mongosh >/dev/null 2>&1; then
  use_docker=0
else
  # No mongosh on host; try docker
  if command -v docker >/dev/null 2>&1 && docker ps --format '{{.Names}}' | grep -q "^${MONGO_CONTAINER}$"; then
    use_docker=1
  else
    echo "Error: 'mongosh' not found on host and Docker container '${MONGO_CONTAINER}' not running."
    echo "Install mongosh (e.g. 'brew install mongosh') or start the container and re-run."
    exit 1
  fi
fi

if [ "$use_docker" -eq 1 ]; then
  echo "Will run mongosh inside Docker container '${MONGO_CONTAINER}' (certs in ${CERT_DIR_CONTAINER})."
else
  echo "Will run mongosh on host (certs in ${CERT_DIR_HOST})."
fi

# Wait for MongoDB to become available
echo "Waiting for MongoDB (${MONGO_HOST}:${MONGO_PORT}) to become available..."
for i in $(seq 1 ${RETRIES}); do
  if [ "$use_docker" -eq 1 ]; then
    if docker exec "${MONGO_CONTAINER}" mongosh --host "${MONGO_HOST}" --port "${MONGO_PORT}" --tls --tlsCertificateKeyFile "${CERT_DIR_CONTAINER}/mongo.pem" --tlsCAFile "${CERT_DIR_CONTAINER}/ca.pem" --eval 'db.adminCommand("ping")' >/dev/null 2>&1; then
      echo "Mongo is up (inside container)"
      break
    fi
  else
    if mongosh --host "${MONGO_HOST}" --port "${MONGO_PORT}" --tls --tlsCertificateKeyFile "${CERT_DIR_HOST}/mongo.pem" --tlsCAFile "${CERT_DIR_HOST}/ca.pem" --eval 'db.adminCommand("ping")' >/dev/null 2>&1; then
      echo "Mongo is up (host mongosh)"
      break
    fi
  fi
  echo "Waiting... ($i/${RETRIES})"
  sleep ${SLEEP}
done

# Create user JS
cat > /tmp/create_user.js <<JS
db = db.getSiblingDB('${AUTH_DB}');
try {
  db.createUser({
    user: '${USER}',
    pwd: '${PASS}',
    roles: [{ role: '${ROLE}', db: '${ROLE_DB}' }]
  });
  print('User ${USER} created');
} catch (e) {
  if (e.codeName === 'DuplicateKey') {
    print('User ${USER} already exists');
  } else {
    throw e;
  }
}
JS

# Run the create user script (either host mongosh or docker exec)
if [ "$use_docker" -eq 1 ]; then
  # Copy the script into the container and execute there to avoid path issues
  docker cp /tmp/create_user.js "${MONGO_CONTAINER}:/tmp/create_user.js"
  docker exec "${MONGO_CONTAINER}" mongosh \
     --username admin \
     --password adminpassword \
     --authenticationDatabase admin \
     --tls \
     --tlsCertificateKeyFile /etc/ssl/mongo/mongo.pem \
     --tlsCAFile /etc/ssl/mongo/ca.pem \
     /tmp/create_user.js \
     || {
       echo "mongosh inside container failed"
       docker exec "${MONGO_CONTAINER}" rm -f /tmp/create_user.js || true
       exit 1
     }
  docker exec "${MONGO_CONTAINER}" rm -f /tmp/create_user.js || true
else
  mongosh /tmp/create_user.js --host "${MONGO_HOST}" --port "${MONGO_PORT}" --tls --tlsCertificateKeyFile "${CERT_DIR_HOST}/mongo.pem" --tlsCAFile "${CERT_DIR_HOST}/ca.pem" || (echo "mongosh failed" && rm -f /tmp/create_user.js && exit 1)
fi

rm -f /tmp/create_user.js

echo "Done."

