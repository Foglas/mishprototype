#!/usr/bin/env bash
set -eo pipefail

TRUST_STORE_PASS=$1

SCRIPTDIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

PROJECT_ROOT="$(cd "$SCRIPTDIR/.." && pwd)"

cd "$SCRIPTDIR"

KEYFILE_DIR="$PROJECT_ROOT/keyfile"

mkdir -p "$KEYFILE_DIR"
chmod 700 "$KEYFILE_DIR"

./generate-keyfile.sh "$KEYFILE_DIR"

docker compose -f "$PROJECT_ROOT/docker-compose.yml" down -v
docker compose -f "$PROJECT_ROOT/docker-compose.yml" up -d
