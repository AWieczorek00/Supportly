-- 1. Ustawienie kontenera
ALTER SESSION SET CONTAINER = ORCLCDB;
ALTER SESSION SET "_ORACLE_SCRIPT"=TRUE;

-- 2. Zabij sesje (w bloku PL/SQL, bezpiecznie)
BEGIN
    FOR s IN (SELECT sid, serial# FROM v$session WHERE username = 'SUPPORTLY') LOOP
            EXECUTE IMMEDIATE 'ALTER SYSTEM KILL SESSION ''' || s.sid || ',' || s.serial# || ''' IMMEDIATE';
        END LOOP;
EXCEPTION
    WHEN OTHERS THEN
        NULL; -- Ignoruj błędy przy zabijaniu sesji
END;
/

-- 3. Usuń użytkownika (z obsługą błędu "nie istnieje")
BEGIN
    EXECUTE IMMEDIATE 'DROP USER supportly CASCADE';
EXCEPTION
    WHEN OTHERS THEN
        -- Kod błędu ORA-01918 oznacza "user does not exist"
        IF SQLCODE != -1918 THEN
            RAISE; -- Jeśli to inny błąd, zgłoś go
        END IF;
END;
/
