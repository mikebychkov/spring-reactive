
CREATE TABLE IF NOT EXISTS beer (
    id SERIAL PRIMARY KEY,
    beer_name VARCHAR(255),
    beer_style VARCHAR(255),
    upc VARCHAR(25),
    version INT,
    quantity_on_hand INT,
    price DECIMAL,
    created_date TIMESTAMP,
    last_modified_date TIMESTAMP
);