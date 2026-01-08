#!/usr/bin/env bash
set -euo pipefail


SCRIPTDIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

"$SCRIPTDIR/generate-certs.sh"
"$SCRIPTDIR/generate-keyfile.sh"


docker compose up -d