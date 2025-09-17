#!/bin/sh

echo "Waiting for SQL Server to start..."
DBSTATUS=1
ERRCODE=1
i=0

while [ "$DBSTATUS" -ne 0 ] && [ "$i" -lt 180 ]; do
    echo "Próba $i: DBSTATUS=$DBSTATUS, ERRCODE=$ERRCODE"
    i=$((i+1))

    DBSTATUS=$(/opt/mssql-tools/bin/sqlcmd -h -1 -t 1 -U sa -P $SA_PASSWORD \
      -Q "SET NOCOUNT ON; SELECT ISNULL(SUM(state),0) FROM sys.databases" -C 2>/dev/null)
    ERRCODE=$?

    DBSTATUS=${DBSTATUS:-1}
    ERRCODE=${ERRCODE:-1}

    echo "DBSTATUS: $DBSTATUS"
    echo "ERRCODE: $ERRCODE"

    sleep 1
done

if [ "$DBSTATUS" -ne 0 ] || [ "$ERRCODE" -ne 0 ]; then
    echo "Błąd podczas uruchamiania SQL Servera" >> /usr/config/error.log
    echo "DBSTATUS: $DBSTATUS" >> /usr/config/error.log
    echo "ERRCODE: $ERRCODE" >> /usr/config/error.log

    echo "SQL Server nie wystartował w 180s lub bazy nie są ONLINE"
    exit 1
fi

echo "Configuring the database..."
/opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P $SA_PASSWORD -d master -i /usr/config/setup.sql -C
echo "Database configuration completed."
