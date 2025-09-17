ALTER TABLE EMPLOYEE
    MODIFY PHONE_NUMBER VARCHAR2(9);

ALTER TABLE EMPLOYEE
    ADD CONSTRAINT uq_employee_phone UNIQUE (PHONE_NUMBER);

ALTER TABLE adm_user
    ADD employee_id NUMBER;

ALTER TABLE adm_user
    ADD CONSTRAINT fk_user_employee FOREIGN KEY (employee_id) REFERENCES employee(id);

ALTER TABLE adm_user
    ADD CONSTRAINT uq_user_employee UNIQUE (employee_id);

ALTER TABLE adm_user
    RENAME COLUMN full_name TO username;
