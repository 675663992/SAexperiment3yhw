CREATE DATABASE ContactDB;

USE ContactDB;
drop table Contacts;
CREATE TABLE Contacts (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          address VARCHAR(255),
                          phone VARCHAR(20)
);

SELECT * FROM Contacts;
