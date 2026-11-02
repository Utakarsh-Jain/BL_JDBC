CREATE DATABASE IF NOT EXISTS payroll_service;
USE payroll_service;

DROP TABLE IF EXISTS employee_department;
DROP TABLE IF EXISTS payroll;
DROP TABLE IF EXISTS employee;
DROP TABLE IF EXISTS department;
DROP TABLE IF EXISTS company;

CREATE TABLE company (
    company_id INT AUTO_INCREMENT PRIMARY KEY,
    company_name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE department (
    department_id INT AUTO_INCREMENT PRIMARY KEY,
    department_name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE employee (
    employee_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    gender CHAR(1) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    address VARCHAR(250) NOT NULL DEFAULT 'India',
    start_date DATE NOT NULL,
    company_id INT NOT NULL,
    CONSTRAINT fk_employee_company FOREIGN KEY (company_id) REFERENCES company (company_id),
    CONSTRAINT chk_employee_gender CHECK (gender IN ('M', 'F'))
);

CREATE TABLE payroll (
    payroll_id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id INT NOT NULL UNIQUE,
    basic_pay DECIMAL(15, 2) NOT NULL,
    deductions DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    taxable_pay DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    income_tax DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    net_pay DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    CONSTRAINT fk_payroll_employee FOREIGN KEY (employee_id) REFERENCES employee (employee_id) ON DELETE CASCADE
);

CREATE TABLE employee_department (
    employee_id INT NOT NULL,
    department_id INT NOT NULL,
    PRIMARY KEY (employee_id, department_id),
    CONSTRAINT fk_employee_department_employee FOREIGN KEY (employee_id) REFERENCES employee (employee_id) ON DELETE CASCADE,
    CONSTRAINT fk_employee_department_department FOREIGN KEY (department_id) REFERENCES department (department_id)
);

INSERT INTO company (company_id, company_name) VALUES
    (1, 'BridgeLabz');

INSERT INTO department (department_id, department_name) VALUES
    (1, 'Sales'),
    (2, 'Marketing'),
    (3, 'Engineering'),
    (4, 'Finance'),
    (5, 'HR');

INSERT INTO employee (employee_id, name, gender, phone, address, start_date, company_id) VALUES
    (1, 'Bill', 'M', '9876543210', 'India', '2018-01-01', 1),
    (2, 'Charlie', 'M', '9876543211', 'India', '2019-03-15', 1),
    (3, 'Terisa', 'F', '9876543212', 'India', '2020-06-01', 1);

INSERT INTO payroll (employee_id, basic_pay, deductions, taxable_pay, income_tax, net_pay) VALUES
    (1, 100000.00, 10000.00, 90000.00, 9000.00, 81000.00),
    (2, 120000.00, 12000.00, 108000.00, 10800.00, 97200.00),
    (3, 2000000.00, 200000.00, 1800000.00, 180000.00, 1620000.00);

INSERT INTO employee_department (employee_id, department_id) VALUES
    (1, 1),
    (2, 3),
    (3, 1),
    (3, 2);

SELECT e.employee_id, e.name, e.gender, e.phone, e.address, e.start_date,
       p.basic_pay, p.deductions, p.taxable_pay, p.income_tax, p.net_pay,
       GROUP_CONCAT(d.department_name ORDER BY d.department_name SEPARATOR ', ') AS departments
FROM employee e
JOIN payroll p ON p.employee_id = e.employee_id
LEFT JOIN employee_department ed ON ed.employee_id = e.employee_id
LEFT JOIN department d ON d.department_id = ed.department_id
GROUP BY e.employee_id, e.name, e.gender, e.phone, e.address, e.start_date,
         p.basic_pay, p.deductions, p.taxable_pay, p.income_tax, p.net_pay
ORDER BY e.employee_id;
