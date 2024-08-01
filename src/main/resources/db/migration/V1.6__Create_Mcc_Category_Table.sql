CREATE TABLE mcc_category (
    mcc VARCHAR(4) NOT NULL,
    category_id UUID NOT NULL,
    created_at timestamp NOT NULL,
    updated_at timestamp NOT NULL,
    PRIMARY KEY (mcc),
    FOREIGN KEY (category_id) REFERENCES category(id)
);