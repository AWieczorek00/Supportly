ALTER TABLE client
    DROP COLUMN nip;

ALTER TABLE client
    DROP COLUMN address;

ALTER TABLE client
    DROP COLUMN city;

ALTER TABLE client
    DROP COLUMN zip_code;

ALTER TABLE client
    DROP COLUMN street_number;

ALTER TABLE client
    DROP COLUMN apartment_number;

ALTER TABLE client
    DROP COLUMN name;

ALTER TABLE client
    ADD first_name VARCHAR2(255);

ALTER TABLE client
    ADD second_name VARCHAR2(255);

ALTER TABLE client
    ADD company_id NUMBER;

ALTER TABLE client
    ADD CONSTRAINT fk_client_company FOREIGN KEY (company_id) REFERENCES company(id);
