
CREATE TABLE IF NOT EXISTS users (
                                     id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                     name VARCHAR(100),
    user_name VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100),
    password VARCHAR(100),
    street VARCHAR(100),
    suite VARCHAR(100),
    city VARCHAR(100),
    zipcode VARCHAR(20),
    geo_lng DOUBLE PRECISION,
    geo_lat DOUBLE PRECISION,
    phone VARCHAR(50),
    website VARCHAR(100),
    company_name VARCHAR(100),
    company_catch_phrase VARCHAR(255),
    company_bs VARCHAR(255)
    );


CREATE TABLE IF NOT EXISTS accounts (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id INTEGER NOT NULL,
    name VARCHAR(255) NOT NULL,
    balance DOUBLE PRECISION NOT NULL DEFAULT 0,
    CONSTRAINT fk_accounts_user
        FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS categories (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id INTEGER NOT NULL,
    name VARCHAR(255) NOT NULL,
    CONSTRAINT fk_categories_user
        FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS transactions (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id INTEGER NOT NULL,
    amount DOUBLE PRECISION NOT NULL CHECK (amount > 0),
    type VARCHAR(32) NOT NULL CHECK (
        type IN ('INCOME', 'EXPENSE', 'SAVINGS_FD', 'SAVINGS_RD', 'SAVINGS_STOCKS')
    ),
    account_id INTEGER NOT NULL,
    category_id INTEGER NOT NULL,
    date DATE NOT NULL,
    CONSTRAINT fk_transactions_user
        FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_transactions_account
        FOREIGN KEY (account_id) REFERENCES accounts (id),
    CONSTRAINT fk_transactions_category
        FOREIGN KEY (category_id) REFERENCES categories (id)
);
