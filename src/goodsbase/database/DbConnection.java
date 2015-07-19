package goodsbase.database;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**Gets connection settings from file.<br/>
 * ! Must be initialized with init() when starting application ! <br/>
 * Contains method that creates a new connection using database settings*/
public class DbConnection {
	
	private static String url;
	private static String driverClass;
	
	private static final String propFileName = "database.properties";
	
	private static final Logger log = Logger.getLogger(DbConnection.class.getName());
	
	/**Reads database settings from properties file.
	 * @throws DbInitException*/
	public static void init() throws DbInitException{		
		log.info("Initializing database properties...");
		try(FileInputStream fin = new FileInputStream(propFileName);){
			Properties prop = new Properties();
			prop.load(fin);
			url = prop.getProperty("db.url");
			driverClass = prop.getProperty("db.driver");									
		} catch (FileNotFoundException e) {
			log.log(Level.WARNING, String.format("Cannot find %s file", propFileName), e);
		} catch (IOException e) {
			log.log(Level.WARNING, String.format("Failed reading %s file", propFileName), e);
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
		
		try {
			log.info("Loading database driver...");
			Class.forName(driverClass);
			log.info("Loading database driver OK");
		} catch (ClassNotFoundException e) {
			throw new DbInitException("Failed to load database driver", e);
		}
	}
	
	/**Creates a new database connection
	 * @throws SQLException */
	public static Connection getConnection() throws SQLException{
		return DriverManager.getConnection(url);
	}
	

}
