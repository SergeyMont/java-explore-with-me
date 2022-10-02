CREATE TABLE IF NOT EXISTS endpoints (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    app VARCHAR(255) NOT NULL,
    uri VARCHAR(255) NOT NULL,
    ip VARCHAR(255) NOT NULL,
    timestamp TIMESTAMP,
    CONSTRAINT pk_endpoint PRIMARY KEY (id)
    );