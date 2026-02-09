#!/usr/bin/env bash
set -eo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"


"/$SCRIPT_DIR/generate-mongo-cert.sh" "mongo-main" "$SCRIPT_DIR/../mongo/certs-main"
"/$SCRIPT_DIR/generate-mongo-cert.sh" "mongo-replica-1" "$SCRIPT_DIR/../mongo/certs-replica-1"
"/$SCRIPT_DIR/generate-mongo-cert.sh" "mongo-replica-2" "$SCRIPT_DIR/../mongo/certs-replica-2"