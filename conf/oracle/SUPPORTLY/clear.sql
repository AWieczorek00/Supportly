-- 1. Ustawienie kontenera na ORCLCDB
ALTER SESSION SET CONTAINER = ORCLCDB;

-- 2. Ważne: Pozwala na operacje na użytkownikach bez "C##" w nazwie w głównym kontenerze
ALTER SESSION SET "_ORACLE_SCRIPT"=TRUE;

-- 3. Ignoruj błędy (np. jeśli użytkownik nie istnieje)
        WHENEVER SQLERROR CONTINUE;

-- 4. Zabij aktywne sesje użytkownika (aby zwolnić blokady)
BEGIN
            FOR s IN (SELECT sid, serial# FROM v$session WHERE username = 'SUPPORTLY') LOOP
    EXECUTE IMMEDIATE 'ALTER SYSTEM KILL SESSION ''' || s.sid || ',' || s.serial# || ''' IMMEDIATE';
END LOOP;
END;
/

-- 5. Usuń TYLKO użytkownika i jego obiekty (tabele itp.)
-- Tablespace SUPPORTLY_TS pozostaje nienaruszony.
DROP USER supportly CASCADE;

-- 6. Przywróć normalne reagowanie na błędy
        WHENEVER SQLERROR EXIT SQL.SQLCODE;