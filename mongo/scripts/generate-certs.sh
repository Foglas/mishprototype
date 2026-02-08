#!/usr/bin/env bash
set -eo pipefail

# Check if first argument exists
if [ $# -lt 1 ] || [ -z "$1" ]; then
  echo "Usage: $0 <truststore-password>"
  exit 1
fi

# Assign first argument to variable
TRUST_STORE_PASS="$1"
MONGO_SERVER_CN=$2
MONGO_CA_CN=$3
OUTPUT_DIR=$4

echo "Using truststore password: $TRUST_STORE_PASS"

SCRIPTDIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
OUTDIR="${OUTPUT_DIR:-${SCRIPTDIR}/../certs}"
mkdir -p "${OUTDIR}"
cd "${OUTDIR}"

if ! command -v openssl >/dev/null 2>&1; then
  echo "Please install openssl and re-run this script."
  exit 1
fi

rm -rf -- "${OUTDIR:?}/"*


CA_CN=${MONGO_CA_CN:-mongo-cluster.local CA}    # CA certificate CN
SERVER_CN=${MONGO_SERVER_CN:-mongo-main}        # Server certificate CN
SAN="DNS:localhost,DNS:${SERVER_CN},DNS:mongo-replica-1,DNS:mongo-replica-2,IP:127.0.0.1"

echo "Generating CA (CN=${CA_CN}) and server cert (CN=${SERVER_CN}) in ${OUTDIR}"

# Generate CA
openssl genrsa -out ca.key 4096
openssl req -x509 -new -nodes -key ca.key -sha256 -days 3650 \
  -subj "/CN=${CA_CN}" -out ca.pem

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

# import into trustStore.jks
TRUSTSTORE="truststore.jks"

ALIAS="ca"
CA_PEM="${OUTDIR}/ca.pem"

if ! command -v keytool >/dev/null 2>&1; then
  echo "keytool not found (JDK/JRE missing)"
  exit 1
fi

if [ ! -f "$CA_PEM" ]; then
  echo "CA certificate not found: $CA_PEM"
  exit 1
fi

if keytool -list \
    -keystore "$TRUSTSTORE" \
    -storepass "$TRUST_STORE_PASS" \
    -alias "$ALIAS" >/dev/null 2>&1; then

  echo "Alias '$ALIAS' already exists in truststore – deleting"
  keytool -delete \
    -alias "$ALIAS" \
    -keystore "$TRUSTSTORE" \
    -storepass "$TRUST_STORE_PASS"
else
  echo "Alias '$ALIAS' does not exist – importing"
fi

keytool -importcert \
  -trustcacerts \
  -alias "$ALIAS" \
  -file "$CA_PEM" \
  -keystore "$TRUSTSTORE" \
  -storepass "$TRUST_STORE_PASS" \
  -noprompt

echo "CA certificate imported successfully into $TRUSTSTORE"
