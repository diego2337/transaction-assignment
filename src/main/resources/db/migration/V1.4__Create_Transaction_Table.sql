CREATE TABLE "transaction" (
    id UUID NOT NULL,
    account_id varchar(255) NOT NULL,
    category_id UUID NOT NULL,
    merchant_id UUID NOT NULL,
    mcc varchar(4) NOT NULL,
    total_amount float NOT NULL,
    created_at timestamp NOT NULL,
    updated_at timestamp NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (account_id) REFERENCES account(id),
    FOREIGN KEY (merchant_id) REFERENCES merchant(id),
    FOREIGN KEY (category_id) REFERENCES category(id)
);