ALTER TABLE client
    DROP COLUMN nip,
    DROP COLUMN address,
    DROP COLUMN city,
    DROP COLUMN zip_code,
    DROP COLUMN street_number,
    DROP COLUMN apartment_number,
    DROP COLUMN name;

ALTER TABLE client
    ADD COLUMN first_name VARCHAR(255),
    ADD COLUMN second_name VARCHAR(255),
    ADD COLUMN company_id INT REFERENCES company(id);

