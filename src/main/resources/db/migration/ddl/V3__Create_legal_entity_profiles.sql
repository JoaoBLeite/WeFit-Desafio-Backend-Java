-- Create table
CREATE TABLE legal_entity_profiles (
    id VARCHAR(36) PRIMARY KEY,
    cnpj VARCHAR(18) NOT NULL UNIQUE,
    responsible_cpf VARCHAR(14) NOT NULL,
    name VARCHAR(100) NOT NULL,
    cell_phone VARCHAR(100),
    phone_number VARCHAR(100),
    email VARCHAR(255) UNIQUE,
    address_id VARCHAR(36) NOT NULL,
    created_at BIGINT NOT NULL,
    last_updated_at BIGINT NOT NULL,

    -- Foreign Key Constraint
    CONSTRAINT fk_legal_entity_profile_address_id
        FOREIGN KEY (address_id)
        REFERENCES addresses (id)
        ON DELETE RESTRICT
    );
);

-- Create indexes
CREATE INDEX idx_legal_entity_profile_cnpj ON legal_entity_profiles(cnpj);
CREATE INDEX idx_legal_entity_profile_responsible_cpf ON legal_entity_profiles(responsible_cpf);
CREATE INDEX idx_legal_entity_profile_responsible_email ON legal_entity_profiles(email);