ALTER TABLE EMPLOYEE
    ALTER COLUMN PHONE_NUMBER VARCHAR(9);

ALTER TABLE EMPLOYEE
    ADD CONSTRAINT uq_employee_phone UNIQUE (PHONE_NUMBER);

ALTER TABLE adm_user
    ADD employee_id INT;

ALTER TABLE adm_user
    ADD CONSTRAINT fk_user_employee FOREIGN KEY (employee_id) REFERENCES employee(id);

ALTER TABLE adm_user
    ADD CONSTRAINT uq_user_employee UNIQUE (employee_id);

EXEC sp_rename 'adm_user.full_name', 'username', 'COLUMN';
