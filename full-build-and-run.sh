#!/usr/bin/env bash
ROOT="$(dirname -- "$( readlink -f -- "$0"; )")";
cd $ROOT/pmp-tracker
mvn clean package
cd $ROOT
sudo docker-compose up --build --force-recreate --remove-orphans --no-deps
