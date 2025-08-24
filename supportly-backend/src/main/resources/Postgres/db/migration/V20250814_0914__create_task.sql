DROP TABLE task;

CREATE TABLE task
(
    id             SERIAL PRIMARY KEY,
    name           VARCHAR(255) NOT NULL,
    execution_time DATE,
    order_id       BIGINT UNIQUE,
    done           BOOLEAN,

    CONSTRAINT fk_task_order FOREIGN KEY (order_id) REFERENCES "order" (id)
);


CREATE TABLE task_employee
(
    task_id     BIGINT NOT NULL,
    employee_id BIGINT NOT NULL,

    PRIMARY KEY (task_id, employee_id),
    CONSTRAINT fk_task_emp_task FOREIGN KEY (task_id) REFERENCES task (id),
    CONSTRAINT fk_task_emp_employee FOREIGN KEY (employee_id) REFERENCES employee (id)
);