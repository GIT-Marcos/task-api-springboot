-- Creación de las tablas
CREATE TABLE IF NOT EXISTS tasks
(
    id
    BIGSERIAL
    PRIMARY
    KEY,
    description
    VARCHAR
(
    255
) NOT NULL UNIQUE,
    date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completed BOOLEAN NOT NULL DEFAULT FALSE
    );
