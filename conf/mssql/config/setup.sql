IF DB_ID('supportly') IS NULL
    BEGIN
        CREATE DATABASE supportly;
    END
GO

USE supportly;
GO

IF NOT EXISTS (SELECT * FROM sys.server_principals WHERE name = 'supportly')
    BEGIN
        CREATE LOGIN supportly WITH PASSWORD = 'Qwerty.1';
        ALTER LOGIN supportly WITH PASSWORD = 'Qwerty.1', CHECK_POLICY = OFF;
    END
GO

IF NOT EXISTS (SELECT * FROM sys.database_principals WHERE name = 'supportly')
    BEGIN
        CREATE USER supportly FOR LOGIN supportly;
    END
GO
