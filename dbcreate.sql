CREATE table IF NOT EXISTS CATEGORIES (
ID INTEGER PRIMARY KEY,
CAT_NAME TEXT UNIQUE,
CAT_DESCRIPTION TEXT,
CAT_PARENT_ID INTEGER
);

CREATE table IF NOT EXISTS PRODUCTS (
PROD_ID INTEGER PRIMARY KEY,
PROD_NAME TEXT NOT NULL,
PROD_DESCRIPTION TEXT,
PROD_MANUFACTURER TEXT NOT NULL,
PROD_TRADE_MARK TEXT,
PROD_CATEGORY_ID INTEGER NOT NULL,
CONSTRAINT unq UNIQUE (PROD_NAME, PROD_MANUFACTURER, PROD_TRADE_MARK)
);

CREATE table IF NOT EXISTS WH_ITEMS (
WH_ID INTEGER PRIMARY KEY,
WH_PRODUCT_ID INTEGER NOT NULL,
WH_QUANTITY REAL  NOT NULL,
WH_UNITS TEXT  NOT NULL,
WH_PRICE REAL  NOT NULL,
CONSTRAINT unq UNIQUE (WH_PRODUCT_ID, WH_UNITS, WH_PRICE)
);

CREATE table IF NOT EXISTS SUPPLIES (
SUPPLIES_ID INTEGER PRIMARY KEY,
SUPPLIES_DATE INTEGER default CURRENT_TIMESTAMP,
SUPPLIES_TYPE TEXT  NOT NULL,
SUPPLIES_PRODUCT_ID INTEGER  NOT NULL,
SUPPLIES_QUANTITY REAL  NOT NULL,
SUPPLIES_UNITS TEXT  NOT NULL,
SUPPLIES_PRICE REAL  NOT NULL
);

//не дай бог их перепутать их порядок
//вставка нового +
CREATE TRIGGER IF NOT EXISTS update_whitems_1 BEFORE INSERT ON supplies
     WHEN NOT EXISTS(SELECT * FROM wh_items
			WHERE wh_product_id = NEW.supplies_product_id
			AND wh_units = NEW.supplies_units
			AND wh_price = NEW.supplies_price)
			AND NEW.supplies_type = 'ARRIVAL'
BEGIN
    INSERT INTO wh_items(WH_PRODUCT_ID, WH_QUANTITY, WH_UNITS, WH_PRICE)
    VALUES (NEW.supplies_product_id, NEW.supplies_quantity, NEW.supplies_units, NEW.supplies_price);
END
//добавление к существующим +
CREATE TRIGGER IF NOT EXISTS update_whitems_2 BEFORE INSERT ON supplies
     WHEN EXISTS(SELECT * FROM wh_items
			WHERE wh_product_id = NEW.supplies_product_id
			AND wh_units = NEW.supplies_units
			AND wh_price = NEW.supplies_price)
			AND NEW.supplies_type = 'ARRIVAL'
BEGIN
    UPDATE wh_items SET wh_quantity = wh_quantity + NEW.supplies_quantity
    WHERE wh_product_id = NEW.supplies_product_id
			AND wh_units = NEW.supplies_units
			AND wh_price = NEW.supplies_price;
END

//норм вычетание +
CREATE TRIGGER IF NOT EXISTS update_whitems_3 BEFORE INSERT ON supplies
     WHEN EXISTS(SELECT * FROM wh_items
			WHERE wh_product_id = NEW.supplies_product_id
			AND wh_units = NEW.supplies_units
			AND wh_price = NEW.supplies_price
			AND wh_quantity > NEW.supplies_quantity)
			AND NEW.supplies_type = 'WRITEOFF'
BEGIN
    UPDATE wh_items SET wh_quantity = wh_quantity - NEW.supplies_quantity
    WHERE wh_product_id = NEW.supplies_product_id
			AND wh_units = NEW.supplies_units
			AND wh_price = NEW.supplies_price;
END

//норм удаление
CREATE TRIGGER IF NOT EXISTS update_whitems_4 BEFORE INSERT ON supplies
     WHEN EXISTS(SELECT * FROM wh_items
			WHERE wh_product_id = NEW.supplies_product_id
			AND wh_units = NEW.supplies_units
			AND wh_price = NEW.supplies_price
			AND wh_quantity = NEW.supplies_quantity)
			AND NEW.supplies_type = 'WRITEOFF'
BEGIN
    DELETE FROM wh_items
    WHERE wh_product_id = NEW.supplies_product_id
			AND wh_units = NEW.supplies_units
			AND wh_price = NEW.supplies_price;
END

//ошибка вычетание+
CREATE TRIGGER IF NOT EXISTS update_whitems_5 BEFORE INSERT ON supplies
     WHEN EXISTS(SELECT * FROM wh_items
			WHERE wh_product_id = NEW.supplies_product_id
			AND wh_units = NEW.supplies_units
			AND wh_price = NEW.supplies_price
			AND wh_quantity < NEW.supplies_quantity)
			AND NEW.supplies_type = 'WRITEOFF'
BEGIN
    SELECT RAISE(ABORT, 'insufficient quantity');
END

//ошибка
CREATE TRIGGER IF NOT EXISTS update_whitems_6 BEFORE INSERT ON supplies
     WHEN NOT EXISTS(SELECT * FROM wh_items
			WHERE wh_product_id = NEW.supplies_product_id
			AND wh_units = NEW.supplies_units
			AND wh_price = NEW.supplies_price)
			AND NEW.supplies_type = 'WRITEOFF'
BEGIN
   SELECT RAISE(ABORT, 'insufficient quantity');
END