#!/bin/sh
/usr/config/configure-db.sh &
/opt/mssql/bin/sqlservr
