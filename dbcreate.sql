CREATE table IF NOT EXISTS CATEGORIES (
ID INTEGER PRIMARY KEY,
NAME TEXT UNIQUE,
DESCRIPTION TEXT,
PARENT_ID INTEGER
);

CREATE table IF NOT EXISTS PRODUCTS (
ID INTEGER PRIMARY KEY,
NAME TEXT,
DESCRIPTION TEXT,
MANUFACTURER TEXT,
TRADE_MARK TEXT,
CATEGORY_ID INTEGER
);

CREATE table IF NOT EXISTS WH_ITEMS (
ID INTEGER PRIMARY KEY,
PRODUCT_ID INTEGER,
QUANTITY BLOB,
UNITS TEXT,
PRICE BLOB
);

CREATE table IF NOT EXISTS SUPPLIES (
ID INTEGER PRIMARY KEY,
DATE INTEGER,
TYPE TEXT,
PRODUCT_ID INTEGER,
QUANTITY BLOB,
UNITS TEXT,
PRICE BLOB
);