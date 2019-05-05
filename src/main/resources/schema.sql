CREATE TABLE IF NOT EXISTS Receipt
(
    id    IDENTITY,
    date  DATE         NOT NULL,
    place VARCHAR(100) NOT NULL,
    sum   DECIMAL      NOT NULL,
    buyer VARCHAR(100),
    image BLOB
);

CREATE TABLE IF NOT EXISTS Purchase
(
    id         IDENTITY,
    name       VARCHAR(100),
    quantity   SMALLINT NOT NULL,
    price      DECIMAL  NOT NULL,
    type       VARCHAR(30),
    receipt_id BIGINT   NOT NULL,
    FOREIGN KEY (receipt_id) REFERENCES receipt (id) ON DELETE CASCADE
);