DROP TABLE people IF EXISTS;
DROP TABLE people_upper_case IF EXISTS;
DROP TABLE temp IF EXISTS;
DROP TABLE digit IF EXISTS;

CREATE TABLE people  (
    person_id BIGINT IDENTITY NOT NULL PRIMARY KEY,
    first_name VARCHAR(20),
    last_name VARCHAR(20)
);

CREATE TABLE people_upper_case  (
    person_id BIGINT IDENTITY NOT NULL PRIMARY KEY,
    first_name VARCHAR(20),
    last_name VARCHAR(20)
);

CREATE TABLE digit(num integer);

CREATE TABLE temp(num integer);