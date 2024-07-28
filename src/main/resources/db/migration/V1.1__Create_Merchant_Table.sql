CREATE TABLE merchant (
    id UUID NOT NULL,
    name varchar(255) NOT NULL,
    mcc varchar(4) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (mcc) REFERENCES category(mcc)
);