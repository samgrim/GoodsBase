package goodsbase.qserver;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**Can execute SELECT request
 * 
 * @author Daria
 */
class QueryTask implements ResultDbTask, AutoCloseable {

	/**@throws UnacceptableRequestException in case of invalid request*/
	public QueryTask(QRequest request) {
		if (request.getType()!=QRequest.Type.SELECT)
			throw new UnacceptableRequestException("Request type must be SELECT");
		if(request.getBody().size()!=1)
			throw new UnacceptableRequestException("Invalid number of queries");
		this.query = request.getBody().get(0);
	}

	@Override
	public void execute(Connection c) {
		try { 			
			stat = c.createStatement();
			result = stat.executeQuery(query);
		} catch (SQLException e) {
			exceptions.add(e);
			log.log(Level.WARNING, "Exception caught while executing query", e);
		} 
		complete = true;		
	}

	@Override
	public ResultSet getResult() {
		return result;
	}

	@Override
	public boolean isComplete() {
		return complete;
	}

	@Override
	public List<SQLException> getExceptions() {
		return exceptions;
	}
	
	/**Closes the task*/
	public void close() throws SQLException {
		stat.close();
	}
	
	protected void setResult(ResultSet res){
		this.result = res;
	}
	
	protected String getQuery(){
		return query;
	}
	
	protected Statement getStatement(){
		return stat;
	}
	
	protected void addException(SQLException e) {
		exceptions.add(e);
	}

	private static final Logger log = Logger.getLogger(QueryTask.class.getName());
	private String query;
	private List<SQLException> exceptions = new LinkedList<SQLException>();
	private boolean complete = false;
	private ResultSet result;
	protected Statement stat;
}
