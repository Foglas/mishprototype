#!/usr/bin/env bash
set -euo pipefail

SCRIPTDIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
OUTDIR="${1:-${SCRIPTDIR}/../certs}"
mkdir -p "${OUTDIR}"
cd "${OUTDIR}"

openssl rand -base64 756 > keyfile
chmod 400 keyfile