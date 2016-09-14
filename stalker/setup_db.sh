#!/usr/bin/env bash

set -e -o pipefail

dbname="guap"

echo "
    CREATE DATABASE $dbname;
" | psql

echo "
    CREATE TABLE prices (
        avg_24h decimal,
        ask decimal,
        bid decimal,
        last decimal,
        time timestamp,
        volume_btc decimal,
        volume_percent decimal
    );
" | psql "dbname=$dbname"