CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS products (
    id SERIAL PRIMARY KEY,
    productname VARCHAR(100) NOT NULL,
    stock INTEGER NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    minimum_stock DECIMAL(10,2) DEFAULT 0,
    status VARCHAR(20) DEFAULT 'active'
);