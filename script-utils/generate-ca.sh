#!/usr/bin/env bash
set -eo pipefail

TRUST_STORE_PASS=$1

CA_DIR=${2:-"../authoritive"}

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

mkdir -p "$CA_DIR"

cd "$CA_DIR"
openssl genrsa -out ca.key 4096
openssl req -x509 -new -nodes -key ca.key -sha256 -days 3650 \
  -subj "/CN=mish-mongo}" -out ca.pem


cd "$SCRIPT_DIR"
./import-into-truststore.sh "$TRUST_STORE_PASS" "$CA_DIR/ca.pem" "$CA_DIR"