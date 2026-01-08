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

# -------------------------------
# Common names and SANs
# -------------------------------
CA_CN=${MONGO_CA_CN:-mongo-cluster.local CA}    # CA certificate CN
SERVER_CN=${MONGO_SERVER_CN:-mongo-tls}        # Server certificate CN
SAN="DNS:localhost,DNS:${SERVER_CN},IP:127.0.0.1"

echo "Generating CA (CN=${CA_CN}) and server cert (CN=${SERVER_CN}) in ${OUTDIR}"

# -------------------------------
# Generate CA
# -------------------------------
openssl genrsa -out ca.key 4096
openssl req -x509 -new -nodes -key ca.key -sha256 -days 3650 \
  -subj "/CN=${CA_CN}" -out ca.pem

# -------------------------------
# Generate server key + CSR
# -------------------------------
openssl genrsa -out server.key 4096

cat > server.cnf <<EOF
[req]
distinguished_name = req_distinguished_name
req_extensions = v3_req
prompt = no

[req_distinguished_name]
CN = ${SERVER_CN}

[v3_req]
subjectAltName = ${SAN}
EOF

openssl req -new -key server.key -out server.csr -config server.cnf

# -------------------------------
# Sign server cert with CA
# -------------------------------
cat > v3ext.cnf <<EOF
subjectAltName = ${SAN}
extendedKeyUsage = serverAuth, clientAuth
EOF

openssl x509 -req -in server.csr -CA ca.pem -CAkey ca.key -CAcreateserial \
  -out server.crt -days 3650 -sha256 -extfile v3ext.cnf

# -------------------------------
# Combine key + cert for mongod
# -------------------------------
cat server.key server.crt > mongo.pem
chmod 400 mongo.pem
chmod 444 ca.pem

# -------------------------------
# Optional keyfile for internal cluster auth
# -------------------------------
openssl rand -base64 756 > keyfile
chmod 400 keyfile

# -------------------------------
# Cleanup
# -------------------------------
rm -f server.csr server.key server.cnf v3ext.cnf ca.srl || true

echo "Generated certs in ${OUTDIR}:"
ls -la "${OUTDIR}"

echo
echo "Next steps:"
echo "1) Mount 'mongo.pem' and 'ca.pem' into your MongoDB container"
echo "2) Use 'mongo.pem' as 'tlsCertificateKeyFile' and 'ca.pem' as 'tlsCAFile'"
echo "3) Ensure 'tls.mode' is 'requireTLS' in mongod.conf"
echo "4) For Java clients, import 'ca.pem' into a trust store or SSLContext to validate the server certificate"
