echo "Waiting for SQL Server to start..."
i=0
until /opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P $SA_PASSWORD -Q "SELECT 1" -b &>/dev/null || [ $i -ge 180 ]; do
    i=$((i+1))
    echo "Waiting... ($i)"
    sleep 1
done

if [ $i -ge 180 ]; then
    echo "SQL Server did not start in time."
    exit 1
fi

echo "Configuring the database..."
/opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P $SA_PASSWORD -d master -i /usr/config/setup.sql -C || echo "Setup already applied"
echo "Database configuration completed."
