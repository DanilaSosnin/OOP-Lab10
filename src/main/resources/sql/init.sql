CREATE DATABASE IF NOT EXISTS lab10_db;
USE lab10_db;

CREATE TABLE tutors (
                        ID INT PRIMARY KEY AUTO_INCREMENT,
                        FIRSTNAME VARCHAR(50) NOT NULL,
                        LASTNAME VARCHAR(50) NOT NULL,
                        SUBJECT VARCHAR(100) NOT NULL,
                        EXPERIENCE INT(3) NOT NULL,
                        PRICE DECIMAL(10,2) NOT NULL,
                        PHONENUMBER VARCHAR(45) NOT NULL
);

INSERT INTO tutors (FIRSTNAME, LASTNAME, SUBJECT, EXPERIENCE, PRICE, PHONENUMBER)
VALUES
    ('John', 'Doe', 'Mathematics', 5, 25.00, '+1234567890'),
    ('Jane', 'Smith', 'Physics', 3, 20.00, '+0987654321');