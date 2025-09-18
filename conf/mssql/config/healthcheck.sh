#!/bin/sh
# healthcheck.sh

SERVER="localhost"
USER="sa"
PASS="${SA_PASSWORD}"
DB="master"
SQLCMD_BIN="/opt/mssql-tools18/bin/sqlcmd"

MAX_RETRIES=30
SLEEP_SECONDS=5
RETRY_COUNT=0

echo ">>> Waiting for SQL Server to start..."

until $SQLCMD_BIN -S "$SERVER" -U "$USER" -P "$PASS" -d "$DB" -C -Q "SELECT 1" >/dev/null 2>&1
do
    RETRY_COUNT=$((RETRY_COUNT+1))
    if [ "$RETRY_COUNT" -ge "$MAX_RETRIES" ]; then
        echo "SQL Server is not responding after $((MAX_RETRIES*SLEEP_SECONDS)) seconds"
        exit 1
    fi
    echo "SQL Server not ready yet, retry $RETRY_COUNT/$MAX_RETRIES..."
    sleep $SLEEP_SECONDS
done

echo ">>> Checking database 'supportly' state..."
STATUS=$($SQLCMD_BIN -S "$SERVER" -U "$USER" -P "$PASS" -d "$DB" -C -h -1 -W -s "" -Q \
    "SELECT state_desc FROM sys.databases WHERE name='supportly'" 2>/dev/null)

if [ "$STATUS" != "ONLINE" ]; then
    echo "Database 'supportly' is not ONLINE, current state: '$STATUS'"
    exit 1
fi

echo ">>> Checking if user 'supportly' exists..."
USER_EXISTS=$($SQLCMD_BIN -S "$SERVER" -U "$USER" -P "$PASS" -d "$DB" -C -h -1 -W -s "" -Q \
    "SELECT COUNT(*) FROM sys.database_principals WHERE name='supportly'" 2>/dev/null)

if [ "$USER_EXISTS" = "0" ]; then
    echo "User 'supportly' does not exist"
    exit 1
fi

echo ">>> SQL Server and database 'supportly' are ready."
exit 0
