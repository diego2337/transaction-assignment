CREATE TABLE "transaction" (
    id UUID NOT NULL,
    account_id varchar(255) NOT NULL,
    mcc varchar(4) NOT NULL,
    merchant varchar(255) NOT NULL,
    total_amount float NOT NULL,
    created_at timestamp NOT NULL,
    updated_at timestamp NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (account_id) REFERENCES account(id),
    FOREIGN KEY (mcc) REFERENCES category(mcc)
);