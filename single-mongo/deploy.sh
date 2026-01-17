#!/usr/bin/env bash
set -euo pipefail

BASE_DIR=$(pwd)
TLS_DIR="$BASE_DIR/tls"
CONTAINER_NAME="mongodb-prod"   # Change to your container name
CONTAINER_TLS_PATH="/etc/mongo/tls"  # Path inside container where TLS files are mounted

echo "=== Creating TLS folder ==="
mkdir -p "$TLS_DIR"

# 1) Generate CA if missing
if [[ ! -f "$TLS_DIR/ca.pem" ]]; then
  echo "--- Generating CA ---"
  openssl genrsa -out "$TLS_DIR/ca.key" 4096
  openssl req -x509 -new -nodes -key "$TLS_DIR/ca.key" -sha256 -days 3650 \
    -subj "/CN=MyMongoCA" -out "$TLS_DIR/ca.pem"
fi

# 2) Prepare server.cnf with proper SANs
SERVER_CNF="$TLS_DIR/server.cnf"
if [[ ! -f "$SERVER_CNF" ]]; then
  cat > "$SERVER_CNF" <<'EOF'
[ req ]
default_bits       = 4096
prompt             = no
default_md         = sha256
req_extensions     = req_ext
distinguished_name = dn

[ dn ]
C  = US
ST = State
L  = City
O  = MyCompany
OU = Database
CN = mongodb

[ req_ext ]
subjectAltName = @alt_names

[ alt_names ]
DNS.1 = mongodb
DNS.2 = mongodb-prod
DNS.3 = localhost
IP.1  = 127.0.0.1
EOF
fi

# 3) Generate server certificate (key+CSR+crt+combined pem)
if [[ ! -f "$TLS_DIR/server.pem" ]]; then
  echo "--- Generating Server Certificate ---"
  openssl genrsa -out "$TLS_DIR/server.key" 4096
  openssl req -new -key "$TLS_DIR/server.key" -out "$TLS_DIR/server.csr" -config "$SERVER_CNF"
  openssl x509 -req -in "$TLS_DIR/server.csr" -CA "$TLS_DIR/ca.pem" -CAkey "$TLS_DIR/ca.key" \
    -CAcreateserial -out "$TLS_DIR/server.crt" -days 365 -sha256 -extfile "$SERVER_CNF" -extensions req_ext
  cat "$TLS_DIR/server.key" "$TLS_DIR/server.crt" > "$TLS_DIR/server.pem"
  chmod 600 "$TLS_DIR/server.pem"
fi

# 4) Generate client certificate (for mutual TLS)
if [[ ! -f "$TLS_DIR/client.pem" ]]; then
  echo "--- Generating Client Certificate ---"
  openssl genrsa -out "$TLS_DIR/client.key" 4096
  openssl req -new -key "$TLS_DIR/client.key" -out "$TLS_DIR/client.csr" -subj "/CN=mongoclient"
  openssl x509 -req -in "$TLS_DIR/client.csr" -CA "$TLS_DIR/ca.pem" -CAkey "$TLS_DIR/ca.key" \
    -CAcreateserial -out "$TLS_DIR/client.crt" -days 365 -sha256
  cat "$TLS_DIR/client.key" "$TLS_DIR/client.crt" > "$TLS_DIR/client.pem"
  chmod 600 "$TLS_DIR/client.pem"
fi

# 5) Start MongoDB container
echo "=== Starting MongoDB container via Docker Compose ==="
docker compose down || true
docker compose up -d

# 6) Wait a few seconds for mongod to start
sleep 5

# 7) Output mongosh connection command
echo ""
echo "=== Deployment Complete ==="
echo "MongoDB is running with TLS and mutual TLS client certificate."
echo ""
echo "Connect using mongosh (host must match one of the SANs: mongodb, mongodb-prod, localhost, 127.0.0.1):"
echo ""
echo "mongosh --host mongodb --port 27017 --tls \\"
echo "  --tlsCAFile $TLS_DIR/ca.pem \\"
echo "  --tlsCertificateKeyFile $TLS_DIR/client.pem \\"
echo "  --username admin --password <password> \\"
echo "  --authenticationDatabase admin"
echo ""
echo "Verify server SANs:"
echo "  docker exec -it $CONTAINER_NAME openssl x509 -in $CONTAINER_TLS_PATH/server.pem -noout -text | sed -n '/Subject Alternative Name/,/X509v3/p'"
