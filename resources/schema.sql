CREATE TABLE IF NOT EXISTS customer (
    customer_id INT PRIMARY KEY,
    customer_limit BIGINT NOT NULL,
    customer_balance BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS transaction (
    customer_id INT NOT NULL,
    amount BIGINT NOT NULL,
    type VARCHAR(20) NOT NULL,
    description VARCHAR(255) NOT NULL,
    requested_at TIMESTAMP NOT NULL
);