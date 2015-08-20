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
	
	/**Reads database settings from properties file and applies them.
	 * @throws DbInitException*/
	public static void init() throws DbInitException{		
		log.info("Initializing database properties...");
		try(InputStream in = DbConnection.class.getResourceAsStream(propResName);){
			Properties prop = new Properties();
			prop.load(in);
			/*initializing params*/
			url = prop.getProperty("db.url");
			driverClass = prop.getProperty("db.driver");									
		} catch (IOException e) {
			log.log(Level.WARNING, String.format("Failed reading %s resource", propResName), e);
		}		
		/*even if there's no exceptions, 
		 * properties may be not configured properly*/
		if(url != null && driverClass != null) {
			log.info("Database properties OK");				
		} else { 
			if (url == null) {
				log.warning("Missing property db.url");
			} 
			if (driverClass == null) {
				log.warning("Missing property db.driver");
			}
			throw new DbInitException("Failed to initialize database properties");
		}
		
		/*loading driver*/
		try {
			log.info("Loading database driver...");
			Class.forName(driverClass);
			log.info("Loading database driver OK");
		} catch (ClassNotFoundException e) {
			throw new DbInitException("Failed to load database driver", e);
		}
		
		createTables();
	}
	
	/**Creates a new database connection
	 * @throws SQLException */
	public static Connection getConnection() throws SQLException{
		return DriverManager.getConnection(url);
	}
	
	/*Creates tables within a single transaction*/
	private static void createTables() throws DbInitException{
		Connection conn = null;
		Statement stat = null;
		try {
			conn = getConnection();
			conn.setAutoCommit(false);
			stat = conn.createStatement();
			log.info("Creating tables...");
			stat.executeUpdate("CREATE table IF NOT EXISTS CATEGORIES ("
					+ "ID INTEGER PRIMARY KEY,"
					+ "NAME TEXT UNIQUE,"
					+ "DESCRIPTION TEXT,"
					+ "PARENT_ID INTEGER);");
			stat.executeUpdate("CREATE table IF NOT EXISTS PRODUCTS ("
					+ "ID INTEGER PRIMARY KEY,"
					+ "NAME TEXT,"
					+ "DESCRIPTION TEXT,"
					+ "MANUFACTURER TEXT,"
					+ "TRADE_MARK TEXT,"
					+ "CATEGORY_ID INTEGER,"
					+ "CONSTRAINT unq UNIQUE (NAME, MANUFACTURER, TRADE_MARK));");
			stat.executeUpdate("CREATE table IF NOT EXISTS WH_ITEMS ("
					+ "ID INTEGER PRIMARY KEY,"
					+ "PRODUCT_ID INTEGER,"
					+ "QUANTITY BLOB,"
					+ "UNITS TEXT,"
					+ "PRICE BLOB);");
			stat.executeUpdate("CREATE table IF NOT EXISTS SUPPLIES ("
					+ "ID INTEGER PRIMARY KEY,"
					+ "DATE INTEGER,"
					+ "TYPE TEXT,"
					+ "PRODUCT_ID INTEGER,"
					+ "QUANTITY BLOB,"
					+ "UNITS TEXT,"
					+ "PRICE BLOB);");
			conn.commit();
			log.info("Tables created");
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				log.log(Level.WARNING, "Exception caught when rollback transaction", e);
			}
			throw new DbInitException("Failed to initialize database ", e);			
		} finally {
			try {
				if(stat != null)			
					stat.close();			
				if(conn!=null)
					conn.close();
			} catch (SQLException e) {
				log.log(Level.WARNING, "Failed to close connection", e);
			}
		}				
	}
	
	
	private static String url;
	
	private static String driverClass;
	
	private static final String propResName = "/database.properties";
	
	private static final Logger log = Logger.getLogger(DbConnection.class.getName());

}
