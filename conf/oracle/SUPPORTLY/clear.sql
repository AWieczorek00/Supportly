BEGIN
    -- 1. ZABIJ SESJE
    -- Pętla po wszystkich aktywnych sesjach użytkownika SUPPORTLY
    FOR s IN (SELECT sid, serial# FROM v$session WHERE username = 'SUPPORTLY') LOOP
            BEGIN
                EXECUTE IMMEDIATE 'ALTER SYSTEM KILL SESSION ''' || s.sid || ',' || s.serial# || ''' IMMEDIATE';
            EXCEPTION
                WHEN OTHERS THEN
                    NULL; -- Ignoruj błędy, jeśli sesja zdążyła się sama zamknąć
            END;
        END LOOP;

    -- 2. USUŃ UŻYTKOWNIKA
    BEGIN
        EXECUTE IMMEDIATE 'DROP USER supportly CASCADE';
    EXCEPTION
        WHEN OTHERS THEN
            -- Kod błędu -1918 oznacza "user does not exist"
            IF SQLCODE != -1918 THEN
                RAISE; -- Jeśli to inny błąd (np. brak uprawnień), zgłoś go i przerwij
            END IF;
        -- Jeśli błąd to -1918, nic nie rób (uznajemy za sukces)
    END;
END;
/