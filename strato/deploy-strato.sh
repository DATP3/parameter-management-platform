#!/usr/bin/env bash
STRATO_DIR="$(dirname -- "$( readlink -f -- "$0"; )")";
cd $STRATO_DIR/../pmp-tracker
mvn clean package
cd $STRATO_DIR
sudo docker compose up --build --force-recreate --remove-orphans --no-deps
