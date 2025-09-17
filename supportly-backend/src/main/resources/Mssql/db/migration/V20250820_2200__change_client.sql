ALTER TABLE client
    DROP COLUMN nip,
         address,
         city,
         zip_code,
         street_number,
         apartment_number,
        name;

ALTER TABLE client
    ADD first_name VARCHAR(255),
        second_name VARCHAR(255),
        company_id INT;

ALTER TABLE client
    ADD CONSTRAINT fk_client_company FOREIGN KEY (company_id) REFERENCES company(id);
