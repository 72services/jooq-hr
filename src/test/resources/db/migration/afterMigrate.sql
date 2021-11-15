DELETE from project_employees;
DELETE from project;
DELETE from phone;
DELETE from employee;
DELETE from address;
DELETE from department;

INSERT INTO project (dtype, id, name) VALUES ('DesignProject', 1, 'Neue Website');
INSERT INTO project (dtype, id, name) VALUES ('QualityProject', 2, 'Quality Gate 2');

INSERT INTO department (id, name) VALUES (1, 'HR');

INSERT INTO address (id, city, state, street, zip) VALUES (1, 'Rehag', 'AG', 'Plattenstrasse 26', '5046');
INSERT INTO employee (id, name, salary, address_id, boss_id, department_id) VALUES (1, 'Ursula Friedman', 95000, 1, null, 1);
INSERT INTO phone (id, phonenumber, type, employee_id) VALUES (1, '031 333 11 12', 'WORK', 1);

INSERT INTO department (id, name) VALUES (2, 'IT');

INSERT INTO address (id, city, state, street, zip) VALUES (2, 'Fruthwilen', 'ZH', 'Im Sandbüel 55', '8269');
INSERT INTO employee (id, name, salary, address_id, boss_id, department_id) VALUES (2, 'Hans Boss', 175000, 2, null, 2);
INSERT INTO phone (id, phonenumber, type, employee_id) VALUES (2, '031 333 11 01', 'WORK', 2);

INSERT INTO address (id, city, state, street, zip) VALUES (3, 'Bern', 'BE', 'Rosengasse 1', '3012');
INSERT INTO employee (id, name, salary, address_id, boss_id, department_id) VALUES (3, 'Peter Muster', 88000, 3, 2, 2);
INSERT INTO phone (id, phonenumber, type, employee_id) VALUES (3, '044 450 61 34', 'HOME', 3);
INSERT INTO phone (id, phonenumber, type, employee_id) VALUES (4, '031 333 11 32', 'WORK', 3);

INSERT INTO project_employees (employees_id, projects_id) VALUES (3, 1);

INSERT INTO address (id, city, state, street, zip) VALUES (4, 'Vaugondry', 'VD', 'Via Gabbietta 77', '1423');
INSERT INTO employee (id, name, salary, address_id, boss_id, department_id) VALUES (4, 'Luca Traugott', 72000, 4, 2, 2);
INSERT INTO phone (id, phonenumber, type, employee_id) VALUES (5, '024 656 44 65', 'HOME', 4);
INSERT INTO phone (id, phonenumber, type, employee_id) VALUES (6, '031 333 11 15', 'WORK', 4);

INSERT INTO project_employees (employees_id, projects_id) VALUES (4, 2);

INSERT INTO address (id, city, state, street, zip) VALUES (5, 'Unterstammheim', 'ZH', 'Via Verbano 75', '8476');
INSERT INTO employee (id, name, salary, address_id, boss_id, department_id) VALUES (5, 'Lea Schulze', 72000, 5, 2, 2);
INSERT INTO phone (id, phonenumber, type, employee_id) VALUES (7, '052 527 23 11', 'HOME', 5);
INSERT INTO phone (id, phonenumber, type, employee_id) VALUES (8, '031 333 11 76', 'WORK', 5);

INSERT INTO project_employees (employees_id, projects_id) VALUES (5, 1);

INSERT INTO address (id, city, state, street, zip) VALUES (6, 'Geiss', 'ZH', 'Schliffgasse 64', '6123');
INSERT INTO employee (id, name, salary, address_id, boss_id, department_id) VALUES (6, 'Felix Beyer', 79000, 6, 2, 2);
