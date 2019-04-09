CREATE TABLE IF NOT EXISTS Receipt
(
    id    IDENTITY,
    date  DATE         NOT NULL,
    place VARCHAR(100) NOT NULL,
    sum   DECIMAL      NOT NULL,
    buyer VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS Purchase
(
    id         IDENTITY,
    amount     SMALLINT NOT NULL,
    sum        DECIMAL  NOT NULL,
    name       VARCHAR(100),
    receipt_id BIGINT   NOT NULL,
    FOREIGN KEY (receipt_id) REFERENCES Receipt (id)
);

CREATE TABLE IF NOT EXISTS Type
(
    id   IDENTITY,
    name VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS PurchaseType
(
    purchase_id BIGINT NOT NULL,
    type_id     BIGINT NOT NULL,
    FOREIGN KEY (purchase_id) REFERENCES Purchase (id),
    FOREIGN KEY (type_id) REFERENCES Type (id)
);