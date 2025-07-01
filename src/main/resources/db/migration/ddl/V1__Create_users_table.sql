-- Create table
CREATE TABLE users (
    id VARCHAR(36) PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'MANAGER', 'REGULAR') NOT NULL DEFAULT 'REGULAR',
    status ENUM('ACTIVE', 'DISABLED', 'BLOCKED') NOT NULL DEFAULT 'ACTIVE',
    created_at BIGINT NOT NULL,
    last_updated_at BIGINT NOT NULL
);

-- Create indexes
CREATE INDEX idx_user_email ON users(email);