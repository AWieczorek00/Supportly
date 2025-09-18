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

# Czekaj aż SQL Server odpowie
#until $SQLCMD_BIN -S "$SERVER" -U "$USER" -P "$PASS" -d "$DB" -Q "SELECT 1" >/dev/null 2>&1
#do
#    RETRY_COUNT=$((RETRY_COUNT+1))
#    if [ "$RETRY_COUNT" -ge "$MAX_RETRIES" ]; then
#        echo "SQL Server is not responding after $((MAX_RETRIES*SLEEP_SECONDS)) seconds"
#        exit 1
#    fi
#    echo "SQL Server not ready yet, retry $RETRY_COUNT/$MAX_RETRIES..."
#    sleep $SLEEP_SECONDS
#done

# Sprawdź, czy baza 'supportly' jest ONLINE
STATUS=$($SQLCMD_BIN -S "$SERVER" -U "$USER" -P "$PASS" -d "$DB" -h -1 -t 1 -Q \
    "SELECT state_desc FROM sys.databases WHERE name='supportly'" 2>/dev/null | tr -d '[:space:]')
    echo "Checking database state..."
    $SQLCMD_BIN -S "$SERVER" -U "$USER" -P "$PASS" -d "$DB" -Q "SELECT state_desc FROM sys.databases WHERE name='supportly'"

if [ "$STATUS" != "ONLINE" ]; then
    echo "Database 'supportly' is not ONLINE, current state: '$STATUS'"
    exit 1
fi

# Sprawdź, czy użytkownik 'supportly' istnieje
USER_EXISTS=$($SQLCMD_BIN -S "$SERVER" -U "$USER" -P "$PASS" -d "$DB" -h -1 -t 1 -Q \
    "SELECT COUNT(*) FROM sys.database_principals WHERE name='supportly'" 2>/dev/null | tr -d '[:space:]')

if [ "$USER_EXISTS" = "0" ]; then
    echo "User 'supportly' does not exist"
    exit 1
fi

echo "SQL Server i baza 'supportly' są gotowe"
exit 0
