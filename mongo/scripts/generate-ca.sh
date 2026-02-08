#!/usr/bin/env bash
set -eo pipefail

# Check if CA_DIR argument exists
if [ $# -lt 1 ] || [ -z "$1" ]; then
  echo "Usage: $0 <CA_DIR>"
  exit 1
fi

OUTDIR="$1"
cd "$OUTDIR"

openssl genrsa -out ca.key 4096
openssl req -x509 -new -nodes -key ca.key -sha256 -days 3650 \
  -subj "/CN=mish-mongo}" -out ca.pem