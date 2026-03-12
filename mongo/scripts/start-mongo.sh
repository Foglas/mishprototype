#!/usr/bin/env bash
set -eo pipefail

TRUST_STORE_PASS=$1
MODE=${2:-prod}

SCRIPTDIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

PROJECT_ROOT="$(cd "$SCRIPTDIR/.." && pwd)"

cd "$SCRIPTDIR"

KEYFILE_DIR="$PROJECT_ROOT/keyfile"

mkdir -p "$KEYFILE_DIR"
chmod 700 "$KEYFILE_DIR"

"$SCRIPTDIR/generate-keyfile.sh" "$KEYFILE_DIR"

if [ "$MODE" = "--dev" ]; then
    ENV_FILE="${PROJECT_ROOT}/dev.env"
else
    ENV_FILE="${PROJECT_ROOT}/.env"
fi

docker compose -f "$PROJECT_ROOT/docker-compose.yml" down -v
docker compose \
  --env-file "$ENV_FILE" \
  -f "$PROJECT_ROOT/docker-compose.yml" \
  up -d