#!/bin/sh
# healthcheck.sh

# Domyślne ustawienia
SERVER="localhost"
USER="sa"
PASS="$SA_PASSWORD"
DB="master"

# Sprawdź czy SQL Server odpowiada
/opt/mssql-tools/bin/sqlcmd -S $SERVER -U $USER -P $PASS -d $DB -Q "SELECT 1" &>/dev/null
if [ $? -ne 0 ]; then
    echo "SQL Server is not responding"
    exit 1
fi

# Sprawdź, czy baza 'supportly' jest ONLINE
STATUS=$(/opt/mssql-tools/bin/sqlcmd -S $SERVER -U $USER -P $PASS -d $DB -h -1 -t 1 -Q \
    "SELECT state_desc FROM sys.databases WHERE name='supportly'" 2>/dev/null)

if [ "$STATUS" != "ONLINE" ]; then
    echo "Database 'supportly' is not ONLINE, current state: $STATUS"
    exit 1
fi

# Sprawdź, czy użytkownik 'supportly' istnieje
USER_EXISTS=$(/opt/mssql-tools/bin/sqlcmd -S $SERVER -U $USER -P $PASS -d $DB -h -1 -t 1 -Q \
    "SELECT COUNT(*) FROM sys.database_principals WHERE name='supportly'" 2>/dev/null)

if [ "$USER_EXISTS" -eq 0 ]; then
    echo "User 'supportly' does not exist"
    exit 1
fi

# Wszystko OK
exit 0
