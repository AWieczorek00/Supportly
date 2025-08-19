CREATE TABLE task
(
    id             BIGSERIAL PRIMARY KEY,
    name           VARCHAR(255) NOT NULL,
    execution_time DATE,
    order_id       BIGINT UNIQUE,
    employee_id    BIGINT UNIQUE,
    done           BOOLEAN,

    CONSTRAINT fk_task_order FOREIGN KEY (order_id) REFERENCES "order" (id),
    CONSTRAINT fk_task_employee FOREIGN KEY (employee_id) REFERENCES employee (id)
);
