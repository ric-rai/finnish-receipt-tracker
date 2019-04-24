CREATE TABLE IF NOT EXISTS Purchase
(
    id         IDENTITY,
    name       VARCHAR(100),
    quantity   SMALLINT NOT NULL,
    price      DECIMAL  NOT NULL,
    type       VARCHAR(30)
);