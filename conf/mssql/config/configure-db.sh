#!/bin/sh

# Wait for SQL Server to start up
echo "Waiting for SQL Server to start..."
DBSTATUS=1
ERRCODE=1
i=0

while [ "$DBSTATUS" -ne 0 ]  && [ "$i" -lt 120 ]; do
      echo "Próba $i: DBSTATUS=$DBSTATUS, ERRCODE=$ERRCODE"

    i=$((i+1))

    DBSTATUS=$(/opt/mssql-tools18/bin/sqlcmd -h -1 -t 1 -U sa -P $SA_PASSWORD -Q "SET NOCOUNT ON; SELECT SUM(state) FROM sys.databases" -C)
    ERRCODE=$?

    DBSTATUS=${DBSTATUS:-1}
    ERRCODE=${ERRCODE:-1}
    echo "DBSTATUS: $DBSTATUS"
    echo "ERRCODE: $ERRCODE"

    sleep 1
done

# Sprawdzenie stanu bazy danych
if [ "$DBSTATUS" -ne 0 ] || [ "$ERRCODE" -ne 0 ]; then
    # Zapisanie aktualnych wartości zmiennych do logu
    echo "Błąd podczas uruchamiania SQL Servera" >> /usr/config/error.log
    echo "DBSTATUS: $DBSTATUS" >> /usr/config/error.log
    echo "ERRCODE: $ERRCODE" >> /usr/config/error.log

    # Wyświetlenie komunikatu na konsoli
    echo "SQL Server took more than 60 seconds to start up or one or more databases are not in an ONLINE state"
    echo "Sprawdź plik error.log w celu uzyskania dodatkowych informacji."

    # Zakończenie skryptu z kodem błędu
    exit 1
fi

# Execute setup SQL script to configure the database
echo "Configuring the database..."
/opt/mssql-tools18/bin/sqlcmd -S localhost -U sa -P $SA_PASSWORD -d master -i /usr/config/setup.sql -C

echo "Database configuration completed."
