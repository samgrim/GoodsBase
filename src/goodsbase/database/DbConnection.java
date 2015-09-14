package goodsbase.database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**Gets connection settings from file.<br/>
 * ! Must be initialized with init() when starting application ! <br/>
 * Contains method that creates a new connection using database settings*/
public class DbConnection {
	
	/**
	 * Reads database settings from properties file and applies them.
	 * 
	 * @throws DbInitException
	 */
	public static void init() throws DbInitException {
		log.info("Initializing database properties...");
		try (InputStream in = DbConnection.class
				.getResourceAsStream(propResName);) {
			Properties prop = new Properties();
			prop.load(in);
			/* initializing params */
			url = prop.getProperty("db.url");
			driverClass = prop.getProperty("db.driver");
		} catch (IOException e) {
			log.log(Level.WARNING,
					String.format("Failed reading %s resource", propResName), e);
		}
		/*
		 * even if there's no exceptions, properties may be not configured
		 * properly
		 */
		if (url != null && driverClass != null) {
			log.info("Database properties OK");
		} else {
			if (url == null) {
				log.warning("Missing property db.url");
			}
			if (driverClass == null) {
				log.warning("Missing property db.driver");
			}
			throw new DbInitException(
					"Failed to initialize database properties");
		}

		/* loading driver */
		try {
			log.info("Loading database driver...");
			Class.forName(driverClass);
			log.info("Loading database driver OK");
		} catch (ClassNotFoundException e) {
			throw new DbInitException("Failed to load database driver", e);
		}

		createTables();
	}
	
	/**
	 * Creates a new database connection
	 * 
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url);
	}

	/* Creates tables and triggers within a single transaction */
	private static void createTables() throws DbInitException {
		Connection conn = null;
		Statement stat = null;
		try {
			conn = getConnection();
			conn.setAutoCommit(false);
			stat = conn.createStatement();
			log.info("Creating tables...");
			stat.executeUpdate("CREATE table IF NOT EXISTS CATEGORIES ("
					+ "CAT_ID INTEGER PRIMARY KEY,"
					+ "CAT_NAME TEXT UNIQUE,"
					+ "CAT_DESCRIPTION TEXT,"
					+ "CAT_PARENT_ID INTEGER);");
			stat.executeUpdate("CREATE table IF NOT EXISTS PRODUCTS ("
					+ "PROD_ID INTEGER PRIMARY KEY,"
					+ "PROD_NAME TEXT NOT NULL,"
					+ "PROD_DESCRIPTION TEXT,"
					+ "PROD_MANUFACTURER TEXT NOT NULL,"
					+ "PROD_TRADE_MARK TEXT,"
					+ "PROD_CATEGORY_ID INTEGER NOT NULL,"
					+ "CONSTRAINT unq UNIQUE (PROD_NAME, PROD_MANUFACTURER, PROD_TRADE_MARK));");
			stat.executeUpdate("CREATE table IF NOT EXISTS WH_ITEMS ("
					+ "WH_ID INTEGER PRIMARY KEY,"
					+ "WH_PRODUCT_ID INTEGER NOT NULL,"
					+ "WH_QUANTITY REAL NOT NULL,"
					+ "WH_UNITS TEXT NOT NULL,"
					+ "WH_PRICE REAL NOT NULL,"
					+ "CONSTRAINT unq UNIQUE (WH_PRODUCT_ID, WH_UNITS, WH_PRICE));");
			stat.executeUpdate("CREATE table IF NOT EXISTS SUPPLIES ("
					+ "SUPPLIES_ID INTEGER PRIMARY KEY,"
					+ "SUPPLIES_DATE TIMESTAMP NOT NULL default CURRENT_TIMESTAMP,"
					+ "SUPPLIES_TYPE TEXT NOT NULL,"
					+ "SUPPLIES_PRODUCT_ID INTEGER NOT NULL,"
					+ "SUPPLIES_QUANTITY REAL NOT NULL,"
					+ "SUPPLIES_UNITS TEXT NOT NULL,"
					+ "SUPPLIES_PRICE REAL NOT NULL);");
			stat.executeUpdate("CREATE table IF NOT EXISTS USERS ("
						+"USER_ID INTEGER PRIMARY KEY, "
						+"USERNAME TEXT UNIQUE NOT NULL, "
						+"PASSWORD TEXT NOT NULL, "
						+"ROLE TEXT NOT NULL"
						+");");
			stat.executeUpdate("CREATE TRIGGER IF NOT EXISTS update_whitems_1 BEFORE INSERT ON supplies "
									+" WHEN NOT EXISTS(SELECT * FROM wh_items "
													+"WHERE wh_product_id = NEW.supplies_product_id "
													+"AND wh_units = NEW.supplies_units "
													+"AND wh_price = NEW.supplies_price) "
													+"AND NEW.supplies_type = 'ARRIVAL' "
								+"BEGIN "
								    +"INSERT INTO wh_items(WH_PRODUCT_ID, WH_QUANTITY, WH_UNITS, WH_PRICE) "
								    +"VALUES (NEW.supplies_product_id, NEW.supplies_quantity, NEW.supplies_units, NEW.supplies_price); "
								+"END ");
			stat.executeUpdate("CREATE TRIGGER IF NOT EXISTS update_whitems_2 BEFORE INSERT ON supplies "
							     +"WHEN EXISTS(SELECT * FROM wh_items "
													+"WHERE wh_product_id = NEW.supplies_product_id "
													+"AND wh_units = NEW.supplies_units "
													+"AND wh_price = NEW.supplies_price) "
														+"AND NEW.supplies_type = 'ARRIVAL' "
								+"BEGIN "
								    +"UPDATE wh_items SET wh_quantity = wh_quantity + NEW.supplies_quantity "
								    +"WHERE wh_product_id = NEW.supplies_product_id "
													+"AND wh_units = NEW.supplies_units "
													+"AND wh_price = NEW.supplies_price; "
								+"END");
			stat.executeUpdate("CREATE TRIGGER IF NOT EXISTS update_whitems_3 BEFORE INSERT ON supplies "
							     +"WHEN EXISTS(SELECT * FROM wh_items "
													+"WHERE wh_product_id = NEW.supplies_product_id "
													+"AND wh_units = NEW.supplies_units "
													+"AND wh_price = NEW.supplies_price "
													+"AND wh_quantity > NEW.supplies_quantity) "
													+"AND NEW.supplies_type = 'WRITEOFF' "
								+"BEGIN "
								    +"UPDATE wh_items SET wh_quantity = wh_quantity - NEW.supplies_quantity "
								    +"WHERE wh_product_id = NEW.supplies_product_id "
													+"AND wh_units = NEW.supplies_units "
													+"AND wh_price = NEW.supplies_price; "
								+"END");
			stat.executeUpdate("CREATE TRIGGER IF NOT EXISTS update_whitems_4 BEFORE INSERT ON supplies "
							     +"WHEN EXISTS(SELECT * FROM wh_items "
													+"WHERE wh_product_id = NEW.supplies_product_id "
													+"AND wh_units = NEW.supplies_units "
													+"AND wh_price = NEW.supplies_price "
													+"AND wh_quantity = NEW.supplies_quantity) "
														+"AND NEW.supplies_type = 'WRITEOFF' "
								+"BEGIN "
								    +"DELETE FROM wh_items "
								    +"WHERE wh_product_id = NEW.supplies_product_id "
													+"AND wh_units = NEW.supplies_units "
													+"AND wh_price = NEW.supplies_price; "
								+"END");
			stat.executeUpdate("CREATE TRIGGER IF NOT EXISTS update_whitems_5 BEFORE INSERT ON supplies "
							     +"WHEN EXISTS(SELECT * FROM wh_items "
													+"WHERE wh_product_id = NEW.supplies_product_id "
													+"AND wh_units = NEW.supplies_units "
													+"AND wh_price = NEW.supplies_price "
													+"AND wh_quantity < NEW.supplies_quantity) "
													+"AND NEW.supplies_type = 'WRITEOFF' "
								+"BEGIN "
								    +"SELECT RAISE(ABORT, 'insufficient quantity'); "
								+"END");
			stat.executeUpdate("CREATE TRIGGER IF NOT EXISTS update_whitems_6 BEFORE INSERT ON supplies "
							     +"WHEN NOT EXISTS(SELECT * FROM wh_items "
													+"WHERE wh_product_id = NEW.supplies_product_id "
													+"AND wh_units = NEW.supplies_units "
													+"AND wh_price = NEW.supplies_price) "
													+"AND NEW.supplies_type = 'WRITEOFF' "
								+"BEGIN "
								   +"SELECT RAISE(ABORT, 'insufficient quantity'); "
								+"END");
			
			stat.executeUpdate("CREATE TRIGGER IF NOT EXISTS delete_category BEFORE DELETE ON categories "
								     +"WHEN EXISTS(SELECT * FROM products "
											+"WHERE prod_category_id = OLD.cat_id) "
								+"BEGIN "
								    + "SELECT RAISE(ABORT, 'category is not empty'); "
								+"END ");

			stat.executeUpdate("CREATE TRIGGER IF NOT EXISTS delete_product BEFORE DELETE ON products "
								    + "WHEN EXISTS(SELECT * FROM wh_items "
											+"WHERE wh_product_id = OLD.prod_id) "
								+"BEGIN "
								    + "SELECT RAISE(ABORT, 'product is not empty'); "
								+"END ");
			
			stat.executeUpdate("CREATE TRIGGER IF NOT EXISTS delete_category_1 BEFORE DELETE ON categories "
								    + "WHEN EXISTS(SELECT * FROM categories "
											+"WHERE cat_parent_id = OLD.cat_id) "
								+"BEGIN "
								   + "SELECT RAISE(ABORT, 'category is not empty'); "
								+"END ");
			conn.commit();
			log.info("Tables created");
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				log.log(Level.WARNING,
						"Exception caught when rollback transaction", e);
			}
			throw new DbInitException("Failed to initialize database ", e);
		} finally {
			try {
				if (stat != null)
					stat.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				log.log(Level.WARNING, "Failed to close connection", e);
			}
		}
	}
	
	/*database url*/
	private static String url;
	/*jdbc driver*/
	private static String driverClass;
	/*configuration file*/
	private static final String propResName = "/database.properties";
	
	private static final Logger log = Logger.getLogger(DbConnection.class.getName());

}
