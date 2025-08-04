ALTER TABLE EMPLOYEE
    ALTER COLUMN PHONE_NUMBER TYPE VARCHAR(9);

ALTER TABLE EMPLOYEE
    ADD CONSTRAINT uq_employee_phone UNIQUE (PHONE_NUMBER);

ALTER TABLE adm_user
    ADD COLUMN employee_id INTEGER;

ALTER TABLE adm_user
    ADD CONSTRAINT fk_user_employee FOREIGN KEY (employee_id) REFERENCES employee(id);

-- opcjonalnie, by wymusić relację 1:1 (czyli jeden użytkownik → jeden employee i odwrotnie)
ALTER TABLE adm_user
    ADD CONSTRAINT uq_user_employee UNIQUE (employee_id);

alter table adm_user
    rename column full_name to username;