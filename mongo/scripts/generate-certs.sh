#!/bin/bash
set -euo pipefail
OUTDIR=/etc/ssl/mongo
mkdir -p "${OUTDIR}"
cd "${OUTDIR}"

echo "Starting cert+keyfile generation in ${OUTDIR}..."

# Install openssl if not present (works on ubuntu base)
if ! command -v openssl >/dev/null 2>&1; then
  echo "openssl not found; attempting to install..."
  apt-get update -y
  DEBIAN_FRONTEND=noninteractive apt-get install -y openssl ca-certificates
fi

CN=${MONGO_CN:-mongo-cluster.local}
SAN_DNS="DNS:shard1,DNS:shard2,DNS:shard3,DNS:cfg1,DNS:cfg2,DNS:cfg3,DNS:mongos,DNS:localhost,DNS:mongo-cluster.local"

echo "CN=${CN}"
echo "SAN=${SAN_DNS}"

# 1) CA
openssl genrsa -out ca.key 4096
openssl req -x509 -new -nodes -key ca.key -sha256 -days 3650 -subj "/CN=${CN} CA" -out ca.pem

# 2) server CSR config
cat > server.cnf <<EOF
[req]
distinguished_name = req_distinguished_name
req_extensions = v3_req
prompt = no

[req_distinguished_name]
CN = ${CN}

[v3_req]
keyUsage = keyEncipherment, dataEncipherment
extendedKeyUsage = serverAuth, clientAuth
subjectAltName = ${SAN_DNS}
EOF

openssl genrsa -out server.key 4096
openssl req -new -key server.key -out server.csr -config server.cnf

cat > v3ext.cnf <<EOF
authorityKeyIdentifier=keyid,issuer
basicConstraints=CA:FALSE
keyUsage = digitalSignature, nonRepudiation, keyEncipherment
extendedKeyUsage = serverAuth, clientAuth
subjectAltName = ${SAN_DNS}
EOF

openssl x509 -req -in server.csr -CA ca.pem -CAkey ca.key -CAcreateserial -out server.crt -days 3650 -sha256 -extfile v3ext.cnf

# combine server key + cert
cat server.key server.crt > mongo.pem

# keyfile for internal auth
openssl rand -base64 756 > keyfile

# permissions (strict)
chmod 400 mongo.pem || true
chmod 444 ca.pem || true
chmod 400 keyfile || true

# cleanup ephemeral files
rm -f server.csr server.crt server.key server.cnf v3ext.cnf ca.srl || true

echo "Wrote files:"
ls -la "${OUTDIR}" || true
echo "Done."
