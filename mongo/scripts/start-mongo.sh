#!/usr/bin/env bash
set -eo pipefail

TRUST_STORE_PASS=$1
MONGO_SERVER_CN=$2
MONGO_CA_CN=$3
OUTPUT_DIR=$4

SCRIPTDIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

"$SCRIPTDIR/generate-certs.sh" "$TRUST_STORE_PASS" "$MONGO_SERVER_CN" "$MONGO_CA_CN" "$OUTDIR"
"$SCRIPTDIR/generate-keyfile.sh"


docker compose up -d