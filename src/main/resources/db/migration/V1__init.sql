CREATE TABLE address (
    id integer NOT NULL,
    city character varying(255),
    state character varying(255),
    street character varying(255),
    zip character varying(255),
    created_date timestamp,
    last_modified_date timestamp,
    version integer default 0
);

CREATE SEQUENCE address_seq
    START WITH 1000
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE department (
    id integer NOT NULL,
    name character varying(255),
    created_date timestamp,
    last_modified_date timestamp,
    version integer default 0
);

CREATE SEQUENCE department_seq
    START WITH 1000
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE employee (
    id integer NOT NULL,
    name character varying(255),
    salary bigint NOT NULL,
    full_time character varying(1) default 'Y',
    address_id integer,
    boss_id integer,
    department_id integer,
    created_date timestamp,
    last_modified_date timestamp,
    version integer default 0
);

CREATE SEQUENCE employee_seq
    START WITH 1000
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE phone (
    id integer NOT NULL,
    phonenumber character varying(255),
    type character varying(255),
    employee_id integer NOT NULL,
    created_date timestamp,
    last_modified_date timestamp,
    version integer default 0
);

CREATE SEQUENCE phone_seq
    START WITH 1000
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE project (
    dtype character varying(31) NOT NULL,
    id integer NOT NULL,
    name character varying(255),
    created_date timestamp,
    last_modified_date timestamp,
    version integer default 0
);

CREATE SEQUENCE project_seq
    START WITH 1000
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


CREATE TABLE project_employees (
    projects_id integer NOT NULL,
    employees_id integer NOT NULL
);

ALTER TABLE address
    ADD CONSTRAINT address_pk PRIMARY KEY (id);

ALTER TABLE department
    ADD CONSTRAINT department_pk PRIMARY KEY (id);

ALTER TABLE employee
    ADD CONSTRAINT employee_pk PRIMARY KEY (id);

ALTER TABLE project_employees
    ADD CONSTRAINT project_employees_pk PRIMARY KEY (projects_id, employees_id);

ALTER TABLE phone
    ADD CONSTRAINT phone_pk PRIMARY KEY (id);

ALTER TABLE project
    ADD CONSTRAINT project_pk PRIMARY KEY (id);

ALTER TABLE phone
    ADD CONSTRAINT phone_employee_fk FOREIGN KEY (employee_id) REFERENCES employee(id);

ALTER TABLE project_employees
    ADD CONSTRAINT project_employees_employee_fk FOREIGN KEY (employees_id) REFERENCES employee(id);

ALTER TABLE employee
    ADD CONSTRAINT employee_department_fk FOREIGN KEY (department_id) REFERENCES department(id);

ALTER TABLE employee
    ADD CONSTRAINT boss_employee_fk FOREIGN KEY (boss_id) REFERENCES employee(id);

ALTER TABLE project_employees
    ADD CONSTRAINT project_employees_project_fk FOREIGN KEY (projects_id) REFERENCES project(id);

ALTER TABLE employee
    ADD CONSTRAINT employee_address_fk FOREIGN KEY (address_id) REFERENCES address(id);
