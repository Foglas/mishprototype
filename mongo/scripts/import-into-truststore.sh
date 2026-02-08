#!/usr/bin/env bash
set -eo pipefail

# Check if TRUST_STORE_PASS argument exists
if [ $# -lt 1 ] || [ -z "$1" ]; then
  echo "Usage: $0 <TRUST_STORE_PASS>"
  exit 1
fi

# Check if CA_DIR argument exists
if [ $# -lt 1 ] || [ -z "$1" ]; then
  echo "Usage: $0 <CA_DIR>"
  exit 1
fi

TRUST_STORE_PASS="$1"
CA_PEM="$2"

ALIAS="ca"
TRUSTSTORE="truststore.jks"


if ! command -v keytool >/dev/null 2>&1; then
  echo "keytool not found (JDK/JRE missing)"
  exit 1
fi

if [ ! -f "$CA_PEM" ]; then
  echo "CA certificate not found: $CA_PEM"
  exit 1
fi

if keytool -list \
    -keystore "$TRUSTSTORE" \
    -storepass "$TRUST_STORE_PASS" \
    -alias "$ALIAS" >/dev/null 2>&1; then

  echo "Alias '$ALIAS' already exists in truststore – deleting"
  keytool -delete \
    -alias "$ALIAS" \
    -keystore "$TRUSTSTORE" \
    -storepass "$TRUST_STORE_PASS"
else
  echo "Alias '$ALIAS' does not exist – importing"
fi

keytool -importcert \
  -trustcacerts \
  -alias "$ALIAS" \
  -file "$CA_PEM" \
  -keystore "$TRUSTSTORE" \
  -storepass "$TRUST_STORE_PASS" \
  -noprompt

echo "CA certificate imported successfully into $TRUSTSTORE"

