CREATE TABLE accounts (
    id BIGINT PRIMARY KEY,
    balance BIGINT NOT NULL
);

INSERT INTO accounts (id, balance)
VALUES
    (1, 1000),
    (2, 1000);