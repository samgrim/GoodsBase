package goodsbase.qserver;

import java.sql.ResultSet;
/**
 * Represents a task that contains execution of sql statements that
 * return ResultSet instance
 * 
 * @author Daria
 */
public interface ResultDbTask extends DbTask {
	/**
	 * @return ResultSet that has been created during execute() or null if
	 *         DbTask has not been executed yet or if execute() has not created
	 *         any ResultSet
	 */	
	ResultSet getResult();
}
