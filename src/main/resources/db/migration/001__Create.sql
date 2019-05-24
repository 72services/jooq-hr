CREATE TABLE hr.department
(
    id   INT         NOT NULL AUTO_INCREMENT,
    name VARCHAR(30) NULL,

    PRIMARY KEY (id)
);


CREATE TABLE hr.employee
(
    id            INT         NOT NULL AUTO_INCREMENT,
    name          VARCHAR(50) NOT NULL,
    salary        DECIMAL(7)  NULL,
    department_id INT         NOT NULL,
    manager_id    INT         NOT NULL,

    PRIMARY KEY (id),

    INDEX fk_employee_department_idx (department_id ASC) VISIBLE,
    INDEX fk_employee_employee_idx (manager_id ASC) VISIBLE,

    CONSTRAINT fk_employee_department
        FOREIGN KEY (department_id)
            REFERENCES hr.department (id),
    CONSTRAINT fk_employee_employee
        FOREIGN KEY (manager_id)
            REFERENCES hr.employee (id)
);

CREATE TABLE hr.address
(
    id          INT         NOT NULL AUTO_INCREMENT,
    street      VARCHAR(50) NULL,
    housenumber VARCHAR(5)  NULL,
    zip         VARCHAR(10) NOT NULL,
    city        VARCHAR(50) NOT NULL,
    employee_id INT         NOT NULL,

    PRIMARY KEY (id),

    INDEX fk_address_employee_idx (employee_id ASC),

    CONSTRAINT fk_address_employee
        FOREIGN KEY (employee_id)
            REFERENCES hr.employee (id)
);

CREATE TABLE hr.phone
(
    id                     INT         NOT NULL AUTO_INCREMENT,
    type                   VARCHAR(20) NOT NULL,
    number                 VARCHAR(30) NOT NULL,
    employee_id            INT         NOT NULL,
    employee_department_id INT         NOT NULL,

    PRIMARY KEY (id),

    INDEX fk_phone_employee_idx (employee_id ASC) ,

    CONSTRAINT fk_phone_employee
        FOREIGN KEY (employee_id)
            REFERENCES hr.employee (id)
);
