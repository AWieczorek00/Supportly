CREATE DATABASE supportly;
GO

USE supportly;
GO

CREATE LOGIN supportly WITH PASSWORD = 'Qwerty.1';
GO

-- Dodajemy linię, aby zapobiec wygasaniu hasła
ALTER LOGIN supportly WITH PASSWORD = 'Qwerty.1', CHECK_POLICY = OFF;
GO

CREATE USER supportly FOR LOGIN supportly;
GO
