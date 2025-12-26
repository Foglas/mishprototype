#!/bin/bash
set -e

CUSTOM_DIR=/container/service/slapd/assets/config/bootstrap/ldif/custom
mkdir -p "$CUSTOM_DIR"

if [ -z "$(ls -A "$CUSTOM_DIR")" ]; then
    echo "Copying LDIF files into custom volume..."
    cp -r /tmp/ldif-to-copy/* "$CUSTOM_DIR"
fi

# Spusť LDAP server na popředí
exec /container/run/process/slapd/run