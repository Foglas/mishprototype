#!/bin/bash
set -xeo pipefail


TRUST_STORE_PASS=$1
REDIS_SERVER_CN=$2
REDIS_CA_CN=$3
OUTPUT_DIR=$4

SCRIPTDIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

"$SCRIPTDIR/generate-certs.sh" "$TRUST_STORE_PASS" "$REDIS_SERVER_CN" "$REDIS_CA_CN" "$OUTPUT_DIR"


docker compose up -d
echo "Everything was set up"