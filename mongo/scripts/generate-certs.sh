#!/usr/bin/env bash
set -eo pipefail

# Check if SERVER_CN argument exists
if [ $# -lt 1 ] || [ -z "$1" ]; then
  echo "Usage: $0 <SERVER_CN>"
  exit 1
fi


# Check if OUTDIR argument exists
if [ $# -lt 2 ] || [ -z "$2" ]; then
  echo "Usage: $1 <OUTDIR>"
  exit 1
fi

# Check if CA_DIR argument exists
if [ $# -lt 3 ] || [ -z "$3" ]; then
  echo "Usage: $2 <CA_DIR>"
  exit 1
fi



SERVER_CN="$1"
OUTDIR="$2"

CA_PEM_SRC="$3/ca.pem"
CA_KEY_SRC="$3/ca.key"

echo "CA_PEM: $CA_PEM CA_KEY $CA_KEY"
shift 1

mkdir -p "${OUTDIR}"
cd "${OUTDIR}"

if ! command -v openssl >/dev/null 2>&1; then
  echo "Please install openssl and re-run this script."
  exit 1
fi

rm -rf -- "${OUTDIR:?}/"*

cp "$CA_PEM_SRC" "$OUTDIR/ca.pem"
cp "$CA_KEY_SRC" "$OUTDIR/ca.key"



CA_CN="mongo-cluster.local"
SAN="DNS:localhost,DNS:${SERVER_CN}"

echo "Generating CA (CN=${CA_CN}) and server cert (CN=${SERVER_CN}) in ${OUTDIR}"

# Generate server key + CSR
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

# Sign server cert with CA
cat > v3ext.cnf <<EOF
subjectAltName = ${SAN}
extendedKeyUsage = serverAuth, clientAuth
EOF

openssl x509 -req -in server.csr -CA ca.pem -CAkey ca.key -CAcreateserial \
  -out server.crt -days 3650 -sha256 -extfile v3ext.cnf


# Combine key + cert for mongod
cat server.key server.crt > mongo.pem
chmod 400 mongo.pem
chmod 444 ca.pem

# Cleanup
rm -f server.csr server.key server.cnf v3ext.cnf ca.srl || true

echo "Generated certs in ${OUTDIR}:"
ls -la "${OUTDIR}"

echo "Starting mongod..."
exec mongod --config /etc/mongo/mongod.conf