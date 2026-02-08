#!/usr/bin/env bash
set -euo pipefail

# Check if CA_DIR argument exists
if [ $# -lt 1 ] || [ -z "$1" ]; then
  echo "Usage: $0 <KEYFILE_DIR>"
  exit 1
fi

OUTDIR="$1"
cd "$OUTDIR"

openssl rand -base64 756 > keyfile
chmod 400 keyfile