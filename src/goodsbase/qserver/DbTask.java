/**
 * Contains classes for server that execute database queries
 * for multiple clients.
 */
package goodsbase.qserver;

import java.sql.Connection;
import java.sql.ResultSet;

/**
 * Represents a task that contains execution of sql statements.
 * @author Daria
 */
public interface DbTask {
	
	/**
	 * Executes task
	 */
	void execute(Connection c);
	
	/**
	 * @return ResultSet that has been created during execute() 
	 * or null if DbTask has not been executed yet or if execute()
	 * has not created any ResultSet
	 */
	ResultSet getResult();
	
	/**
	 * @return true if task execution is complete
	 */
	boolean isComplete();
}
