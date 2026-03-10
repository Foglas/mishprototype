#!/usr/bin/env bash
set -euo pipefail

PRIMARY_HOST="mongo-main"
PORT=27017
REPLICA_SET_NAME="replica-set-1"
CA_FILE="/etc/ssl/mongo/ca.pem"

USE_ADMIN_USER="${MONGO_INITDB_ROOT_USERNAME:-admin}"
USE_ADMIN_PASSWORD="${MONGO_INITDB_ROOT_PASSWORD:-adminpassword}"

NODES=(
  "mongo-main"
  "mongo-replica-1"
  "mongo-replica-2"
)

echo 'Initializing replica set ...'


echo "Waiting for all MongoDB nodes will be up..."

for HOST in "${NODES[@]}"; do
  echo "Checking $HOST..."

  while true; do
    if mongosh "mongodb://$USE_ADMIN_USER:$USE_ADMIN_PASSWORD@$HOST:$PORT/admin?tls=true&tlsCAFile=$CA_FILE" \
      --eval "db.runCommand({ ping: 1 })"; then

      echo "  $HOST is ready!"
      break
    else
      echo " Failed to connect to $HOST!"
      sleep 3
    fi
  done
done

echo "All mongo nodes are up. Initializing replica set..."

mongosh "mongodb://$USE_ADMIN_USER:$USE_ADMIN_PASSWORD@$PRIMARY_HOST:$PORT/?tls=true&tlsCAFile=$CA_FILE" <<EOF
rs.initiate({
  _id: "$REPLICA_SET_NAME",
  members: [
    { _id: 0, host: "mongo-main:27017", priority: 2 },
    { _id: 1, host: "mongo-replica-1:27017", priority: 1 },
    { _id: 2, host: "mongo-replica-2:27017", priority: 1 }
  ]
})
EOF

echo "Replica set initialized!"