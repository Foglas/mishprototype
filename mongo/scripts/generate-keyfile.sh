#!/usr/bin/env bash
set -euo pipefail

KEYFILE_DIR="$1"
KEYFILE_PATH="$KEYFILE_DIR/keyfile"

mkdir -p "$KEYFILE_DIR"

if [ -f "$KEYFILE_PATH" ]; then
    rm -f "$KEYFILE_PATH"
fi

openssl rand -base64 756 > "$KEYFILE_PATH"

chmod 400 "$KEYFILE_PATH"