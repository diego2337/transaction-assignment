CREATE TABLE category (
    mcc varchar(4) NOT NULL,
    category varchar(10) NOT NULL,
    created_at timestamp NOT NULL,
    updated_at timestamp NOT NULL,
    PRIMARY KEY (mcc)
);