CREATE DATABASE supportly;
GO

USE supportly;
GO

-- Tworzymy login, jeśli nie istnieje
IF NOT EXISTS (SELECT * FROM sys.server_principals WHERE name = 'supportly')
    BEGIN
        CREATE LOGIN supportly WITH PASSWORD = 'Qwerty.1';
        ALTER LOGIN supportly WITH PASSWORD = 'Qwerty.1', CHECK_POLICY = OFF;
    END
GO

-- Tworzymy użytkownika w bazie, jeśli nie istnieje
IF NOT EXISTS (SELECT * FROM sys.database_principals WHERE name = 'supportly')
    BEGIN
        CREATE USER supportly FOR LOGIN supportly;
    END
GO

-- Nadajemy pełne uprawnienia (db_owner) w bazie
ALTER ROLE db_owner ADD MEMBER supportly;
GO
