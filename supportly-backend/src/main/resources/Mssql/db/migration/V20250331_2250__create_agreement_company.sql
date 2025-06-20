-- Tworzenie tabeli Address
CREATE TABLE ADDRESS
(
    ID            INT IDENTITY PRIMARY KEY,
    CITY          NVARCHAR(255) NOT NULL,
    STREET        NVARCHAR(255) NOT NULL,
    ZIP_CODE      NVARCHAR(10)  NOT NULL,
    STREET_NUMBER INT           NOT NULL
);

-- Tworzenie tabeli Company
CREATE TABLE COMPANY
(
    ID           INT IDENTITY PRIMARY KEY,
    NAME         NVARCHAR(255)       NOT NULL,
    NIP          NVARCHAR(13) UNIQUE,
    ADDRESS_ID   INT,
    PHONE_NUMBER NVARCHAR(9)         NOT NULL,
    EMAIL        NVARCHAR(255)       NOT NULL,
    REGON        NVARCHAR(14) UNIQUE NOT NULL,
    CONSTRAINT FK_ADDRESS FOREIGN KEY (ADDRESS_ID) REFERENCES ADDRESS (ID) ON DELETE CASCADE
);

CREATE TABLE AGREEMENT
(
    ID               INT IDENTITY PRIMARY KEY,
    COMPANY_ID       INT,
    AGREEMENT_NUMBER NVARCHAR(50)  NOT NULL,
    DATE_FROM        DATE          NOT NULL,
    DATE_TO          DATE          NOT NULL,
    PERIOD           NVARCHAR(255) NOT NULL,
    CONSTRAINT FK_COMPANY FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY (ID) ON DELETE CASCADE
);