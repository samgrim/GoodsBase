package goodsbase.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import goodsbase.model.Warehouse;

public class DataLoader {
	
	private Connection connection;
	
	public DataLoader() throws SQLException {
		connection = DbConnection.getConnection();		
	}

	public void closeConnection() throws SQLException {
		if(connection!=null)	
			connection.close();			
	}
	/*TODO: remove*/
	public void createCats() throws SQLException{
		if(connection!=null) {		
			Statement stat = connection.createStatement();
			stat.executeUpdate("INSERT INTO CATEGORY (NAME, DESCRIPTION) VALUES ('кашка', 'малашка')");
			stat.executeUpdate("INSERT INTO CATEGORY (NAME, DESCRIPTION) VALUES ('сапог', 'дырявый')");
			stat.executeUpdate("INSERT INTO CATEGORY (NAME, DESCRIPTION) VALUES ('телефон', 'лопата')");
			stat.executeUpdate("INSERT INTO CATEGORY (NAME, DESCRIPTION) VALUES ('дикобраз', 'О_О')");
			stat.executeUpdate("INSERT INTO CATEGORY (NAME, DESCRIPTION, PARENT_ID) VALUES ('кот', 'О_О', 5)");
			stat.executeUpdate("INSERT INTO CATEGORY (NAME, DESCRIPTION, PARENT_ID) VALUES ('самсунх', 'О_О', 4)");
			stat.executeUpdate("INSERT INTO CATEGORY (NAME, DESCRIPTION, PARENT_ID) VALUES ('нокия', 'О_О', 4)");
		}
	}

	public Set<Category> getCategories(Warehouse wh) throws SQLException  {
		Set<Category> cat = new HashSet<Category>();
		if(connection!=null) {		
			Statement stat = connection.createStatement();
			ResultSet res = stat.executeQuery("SELECT * FROM CATEGORY");
			while(res.next()) {					
				Category c = new Category(res.getInt("ID"), res.getInt("PARENT_ID"),
									res.getString("NAME"),	res.getString("DESCRIPTION"));
				cat.add(c);
			}				
		}
		return cat;
	}
}
