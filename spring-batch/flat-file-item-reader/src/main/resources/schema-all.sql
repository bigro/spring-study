DROP TABLE flat_file IF EXISTS;

CREATE TABLE flat_file  (
    person_id BIGINT IDENTITY NOT NULL PRIMARY KEY,
    line VARCHAR(20),
    line_number INT
);