package goodsbase.database;

import goodsbase.model.Warehouse;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

public class DataLoader {
	
	private Connection connection;
	
	public void connectDB() throws SQLException{
		connection = DbConnection.getConnection();
	}
	
	public void closeConnection() throws SQLException {
		connection.close();
	}

	public Set<Category> getCategories(Warehouse wh) throws SQLException {
		Statement stat = connection.createStatement();
		ResultSet res = stat.executeQuery("SELECT * FROM CATEGORY");
		Set<Category> cat = new HashSet<Category>();
		while(res.next()) {
			
		Category c = new Category(res.getInt("ID"), res.getInt("PARENT_ID"),
								res.getString("NAME"),	res.getString("DESCRIPTION"));
			cat.add(c);
		}
		return cat;
	}

}
