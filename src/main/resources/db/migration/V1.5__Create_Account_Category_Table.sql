CREATE TABLE account_category (
    account_id varchar(255) NOT NULL,
    category_id UUID NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL,
    PRIMARY KEY (account_id, category_id),
    FOREIGN KEY (account_id) REFERENCES account(id),
    FOREIGN KEY (category_id) REFERENCES category(id)
);