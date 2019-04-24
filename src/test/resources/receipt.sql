CREATE TABLE IF NOT EXISTS receipt
(
    id    IDENTITY,
    date  DATE         NOT NULL,
    place VARCHAR(100) NOT NULL,
    sum   DECIMAL      NOT NULL,
    buyer VARCHAR(100),
    image BLOB
);