-- Crear la tabla Phone
CREATE TABLE IF NOT EXISTS phone (
    id VARCHAR(36) PRIMARY KEY,
    number VARCHAR(255),
    citycode VARCHAR(10),
    contrycode VARCHAR(10)
);

-- Crear la tabla User
CREATE TABLE IF NOT EXISTS "user" (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    password VARCHAR(255),
    created TIMESTAMP,
    modified TIMESTAMP,
    last_login TIMESTAMP,
    token VARCHAR(255),
    is_active BOOLEAN
);

-- Crear la tabla intermedia para la relación OneToMany entre User y Phone
CREATE TABLE IF NOT EXISTS user_phone (
    user_id VARCHAR(36),
    phone_id VARCHAR(36),
    PRIMARY KEY (user_id, phone_id),
    FOREIGN KEY (user_id) REFERENCES "user"(id),
    FOREIGN KEY (phone_id) REFERENCES phone(id)
);
