package goodsbase.qserver;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**Can execute UPDATE and DELETE requests
 * Executes only first query in the request
 * @author Daria
 */
public class UpdateTask implements DbTask {

	/**@throws UnacceptableRequestException in case of invalid request*/
	public UpdateTask(QRequest request) {
		if (request.getType()!=QRequest.Type.UPDATE)
			throw new UnacceptableRequestException("Request type must be UPDATE");
		if(request.getBody().size()!=1)
			throw new UnacceptableRequestException("Invalid number of queries");
		this.query = request.getBody().get(0);
	}

	@Override
	public void execute(Connection c) {
		try { 			
			stat = c.createStatement();
			stat.executeUpdate(query);
		} catch (SQLException e) {
			exceptions.add(e);
			log.log(Level.WARNING, "Exception caught while executing query", e);
		} 
		complete = true;		
	}

	@Override
	public boolean isComplete() {
		return complete;
	}
	
	/**Returns the list of exceptions occurred during execution*/
	public List<SQLException> getExceptions() {
		return exceptions;
	}
	
	private static final Logger log = Logger.getLogger(BatchTask.class.getName());
	private String query;
	private List<SQLException> exceptions = new LinkedList<SQLException>();
	private boolean complete = false;
	protected Statement stat;
}
