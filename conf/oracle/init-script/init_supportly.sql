        WHENEVER SQLERROR EXIT SQL.SQLCODE;

-- Przełączenie do PDB (Pluggable Database)
        ALTER SESSION SET CONTAINER = ORCLPDB1;

-- Tablespace dla aplikacji
        CREATE TABLESPACE SUPPORTLY_TS
    DATAFILE '/opt/oracle/oradata/ORCLCDB/ORCLPDB1/supportly.dbf'
    SIZE 100M
    AUTOEXTEND ON NEXT 50M MAXSIZE 8G
    LOGGING
    EXTENT MANAGEMENT LOCAL UNIFORM SIZE 10M;

-- Tworzenie użytkownika aplikacyjnego
        CREATE USER supportly IDENTIFIED BY "Qwerty_1"
    DEFAULT TABLESPACE SUPPORTLY_TS
    TEMPORARY TABLESPACE TEMP
    PROFILE DEFAULT
    ACCOUNT UNLOCK;

-- Podstawowe uprawnienia
        GRANT CONNECT, RESOURCE TO supportly;
        GRANT UNLIMITED TABLESPACE TO supportly;
        GRANT CREATE SESSION TO supportly;
        GRANT CREATE VIEW TO supportly;

-- Uprawnienia do odczytu metadanych (jeśli aplikacja tego wymaga)
        GRANT SELECT ON SYS.ALL_TAB_COLS TO supportly;
        GRANT SELECT ON SYS.ALL_CONS_COLUMNS TO supportly;
        GRANT SELECT ON SYS.DBA_CONSTRAINTS TO supportly;

                EXIT;
