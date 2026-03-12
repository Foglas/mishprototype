#!/bin/bash
set -xeo pipefail

MODE=${1:-prod}

SCRIPTDIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPTDIR/.." && pwd)"


if [ "$MODE" = "--dev" ]; then
    ENV_FILE="${PROJECT_ROOT}/dev.env"
else
    ENV_FILE="${PROJECT_ROOT}/.env"
fi

docker compose -f "$PROJECT_ROOT/docker-compose.yml" down -v
docker compose --env-file "$ENV_FILE" -f "$PROJECT_ROOT/docker-compose.yml" up -d