DROP TABLE IF EXISTS Person
DROP TABLE IF EXISTS Address

CREATE TABLE Person (
  PersonId INT, 
  FirstName VARCHAR(255), 
  LastName VARCHAR(255)
);

CREATE TABLE Address (
  AddressId INT, 
  PersonId INT, 
  City VARCHAR(255), 
  State VARCHAR(255)
);

TRUNCATE TABLE Person;
INSERT INTO Person (PersonId, LastName, FirstName) VALUES (1, 'Wang', 'Allen');

TRUNCATE TABLE Address;
INSERT INTO Address (AddressId, PersonId, City, State) VALUES (1, 2, 'New York City', 'New York');
insert into person values(2, 'Artem', 'Validzhanov')

