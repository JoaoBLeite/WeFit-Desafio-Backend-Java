-- Create table
CREATE TABLE addresses (
    id VARCHAR(36) PRIMARY KEY NOT NULL,
    cep VARCHAR(9) NOT NULL,
    state VARCHAR(50) NOT NULL,
    city VARCHAR(100) NOT NULL,
    neighborhood VARCHAR(100) NOT NULL,
    street VARCHAR(200) NOT NULL,
    number VARCHAR(20) NOT NULL,
    complement VARCHAR(225)
);