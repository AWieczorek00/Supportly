USE master;
GO

-- 1. Usuwanie bazy danych (jeśli istnieje)
IF EXISTS (SELECT name FROM sys.databases WHERE name = N'supportly')
    BEGIN
        -- Najważniejszy krok: Ustawia bazę w tryb SINGLE_USER i natychmiast zrywa połączenia (odpowiednik KILL SESSION)
        ALTER DATABASE supportly SET SINGLE_USER WITH ROLLBACK IMMEDIATE;

        -- Teraz można bezpiecznie usunąć bazę
        DROP DATABASE supportly;
    END
GO

-- 2. Usuwanie Loginu (konta serwerowego)
IF EXISTS (SELECT name FROM sys.server_principals WHERE name = 'supportly')
    BEGIN
        -- Dodatkowe zabezpieczenie: Zabij sesje użytkownika 'supportly', jeśli jest zalogowany do innych baz (np. master)
        DECLARE @kill_cmd NVARCHAR(MAX) = '';

        SELECT @kill_cmd = @kill_cmd + 'KILL ' + CAST(session_id AS VARCHAR(10)) + ';'
        FROM sys.dm_exec_sessions
        WHERE login_name = 'supportly'
          AND session_id <> @@SPID; -- Nie zabijaj własnej sesji

        -- Wykonaj polecenia KILL
        EXEC sp_executesql @kill_cmd;

        -- Usuń login
        DROP LOGIN supportly;
    END
GO