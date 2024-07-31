CREATE TABLE account_category (
    account_id varchar(255) NOT NULL,
    category_id varchar(4) NOT NULL,
    total_amount float NOT NULL,
    FOREIGN KEY (account_id) REFERENCES account(id),
    FOREIGN KEY (category_id) REFERENCES category(mcc)
);