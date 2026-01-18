#!/bin/bash
./prepareDataToImport.sh

docker compose up -d
echo "Everything was set up"