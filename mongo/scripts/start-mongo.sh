#!/usr/bin/env bash
set -eo pipefail

TRUST_STORE_PASS=$1

SCRIPTDIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

CERT_DIR="$(pwd)/certs"
mkdir -p "$CERT_DIR"
chmod -R 700 "$CERT_DIR"

cd "$CERT_DIR"
"$SCRIPTDIR/generate-ca.sh" "$CERT_DIR"
"$SCRIPTDIR/generate-keyfile.sh" "$CERT_DIR"
"$SCRIPTDIR/import-into-truststore.sh" "$TRUST_STORE_PASS" "$CERT_DIR/ca.pem"

docker compose up -d