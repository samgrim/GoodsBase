package goodsbase.qserver;

import java.sql.Connection;

/**
 * Represents a task that contains execution of sql statements.
 * 
 * @author Daria
 */
public interface DbTask {

	/**
	 * Executes task
	 */
	void execute(Connection c);

	/**
	 * @return true if task execution is complete
	 */
	boolean isComplete();
}
