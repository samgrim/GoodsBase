package goodsbase.qserver;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Represents a task that contains execution of sql statements.
 * 
 * @author Daria
 */
interface DbTask {

	/**
	 * Executes task
	 */
	void execute(Connection c);

	/**
	 * @return true if task execution is complete
	 */
	boolean isComplete();
	

	/**Returns the list of exceptions occurred during execution*/
	List<SQLException> getExceptions();
}
