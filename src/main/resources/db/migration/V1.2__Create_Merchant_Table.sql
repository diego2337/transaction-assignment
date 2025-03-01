CREATE TABLE merchant (
    id UUID NOT NULL,
    name varchar(255) NOT NULL,
    category_id UUID NOT NULL,
    created_at timestamp NOT NULL,
    updated_at timestamp NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (category_id) REFERENCES category(id)
);