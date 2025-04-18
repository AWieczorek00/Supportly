-- Tworzenie tabeli Address
CREATE TABLE ADDRESS
(
    ID            SERIAL PRIMARY KEY,
    CITY          VARCHAR(255) NOT NULL,
    STREET        VARCHAR(255) NOT NULL,
    ZIP_CODE      VARCHAR(10)  NOT NULL,
    STREET_NUMBER INTEGER      NOT NULL
);

-- Tworzenie tabeli Company
CREATE TABLE COMPANY
(
    ID           SERIAL PRIMARY KEY,
    NAME         VARCHAR(255)       NOT NULL,
    NIP          VARCHAR(13) UNIQUE,
    ADDRESS_ID   INTEGER,
    PHONE_NUMBER VARCHAR(9)         NOT NULL,
    EMAIL        VARCHAR(255)       NOT NULL,
    REGON        VARCHAR(14) UNIQUE NOT NULL,
    CONSTRAINT FK_ADDRESS FOREIGN KEY (ADDRESS_ID) REFERENCES ADDRESS (ID) ON DELETE CASCADE
);

CREATE TABLE AGREEMENT
(
    ID               SERIAL PRIMARY KEY,
    COMPANY_ID       INTEGER,
    AGREEMENT_NUMBER VARCHAR(50)  NOT NULL,
    DATE_FROM        DATE         NOT NULL,
    DATE_TO          DATE         NOT NULL,
    PERIOD           VARCHAR(255) NOT NULL,
    CONSTRAINT FK_COMPANY FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY (ID) ON DELETE CASCADE
);