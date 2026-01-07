#!/usr/bin/env bash
set -euo pipefail

# Generates a CA and server certificate suitable for MongoDB TLS, plus a random keyfile.
# Usage: ./generate-certs.sh [OUTPUT_DIR]

SCRIPTDIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
OUTDIR="${1:-${SCRIPTDIR}/../certs}"
mkdir -p "${OUTDIR}"
cd "${OUTDIR}"

if ! command -v openssl >/dev/null 2>&1; then
  echo "Please install openssl and re-run this script."
  exit 1
fi

CN=${MONGO_CN:-mongo-cluster.local}
SAN="DNS:localhost,IP:127.0.0.1,DNS:mongo-cluster.local"

echo "Generating CA and server certs in ${OUTDIR} (CN=${CN})"

# CA
openssl genrsa -out ca.key 4096
openssl req -x509 -new -nodes -key ca.key -sha256 -days 3650 -subj "/CN=${CN} CA" -out ca.pem

# Server key + CSR
openssl genrsa -out server.key 4096

cat > server.cnf <<EOF
[req]
distinguished_name = req_distinguished_name
req_extensions = v3_req
prompt = no

[req_distinguished_name]
CN = ${CN}

[v3_req]
subjectAltName = ${SAN}
EOF

openssl req -new -key server.key -out server.csr -config server.cnf

cat > v3ext.cnf <<EOF
subjectAltName = ${SAN}
extendedKeyUsage = serverAuth, clientAuth
EOF

openssl x509 -req -in server.csr -CA ca.pem -CAkey ca.key -CAcreateserial -out server.crt -days 3650 -sha256 -extfile v3ext.cnf

# Combine key + cert for mongod (PEM with private key first)
cat server.key server.crt > mongo.pem
chmod 400 mongo.pem || true
chmod 444 ca.pem || true

# Optional keyfile for internal auth (permissions 400)
openssl rand -base64 756 > keyfile
chmod 400 keyfile || true

# Cleanup
rm -f server.csr server.crt server.key server.cnf v3ext.cnf ca.srl || true

echo "Generated certs in ${OUTDIR}:"
ls -la "${OUTDIR}"

echo "Done. To use them with docker-compose: run this script, then from project root: docker compose -f mongo/tls/docker-compose.yml up -d"

