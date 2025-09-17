DROP TABLE IF EXISTS task_employee;
DROP TABLE IF EXISTS task;

CREATE TABLE task
(
    id             INT IDENTITY(1,1) PRIMARY KEY,
    name           VARCHAR(255) NOT NULL,
    execution_time DATE,
    order_id       INT UNIQUE,  -- pasuje do [ORDER].ID
    done           BIT,

    CONSTRAINT fk_task_order FOREIGN KEY (order_id) REFERENCES [ORDER](ID)
);

CREATE TABLE task_employee
(
    task_id     INT NOT NULL,  -- pasuje do task.id
    employee_id INT NOT NULL,  -- pasuje do employee.id

    PRIMARY KEY (task_id, employee_id),
    CONSTRAINT fk_task_emp_task FOREIGN KEY (task_id) REFERENCES task(id),
    CONSTRAINT fk_task_emp_employee FOREIGN KEY (employee_id) REFERENCES employee(id)
);