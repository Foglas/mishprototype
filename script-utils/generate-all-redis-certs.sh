#!/usr/bin/env bash
set -eo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

"/$SCRIPT_DIR/generate-redis-cert.sh" "redis-node-1" "$SCRIPT_DIR/../redis/certs-node-1"
"/$SCRIPT_DIR/generate-redis-cert.sh" "redis-node-2" "$SCRIPT_DIR/../redis/certs-node-2"
"/$SCRIPT_DIR/generate-redis-cert.sh" "redis-node-3" "$SCRIPT_DIR/../redis/certs-node-3"
"/$SCRIPT_DIR/generate-redis-cert.sh" "redis-node-4" "$SCRIPT_DIR/../redis/certs-node-4"
"/$SCRIPT_DIR/generate-redis-cert.sh" "redis-node-5" "$SCRIPT_DIR/../redis/certs-node-5"
"/$SCRIPT_DIR/generate-redis-cert.sh" "redis-node-6" "$SCRIPT_DIR/../redis/certs-node-6"
"/$SCRIPT_DIR/generate-redis-cert.sh" "redis-node-7" "$SCRIPT_DIR/../redis/certs-node-7"
"/$SCRIPT_DIR/generate-redis-cert.sh" "redis-node-8" "$SCRIPT_DIR/../redis/certs-node-8"
"/$SCRIPT_DIR/generate-redis-cert.sh" "redis-node-9" "$SCRIPT_DIR/../redis/certs-node-9"
