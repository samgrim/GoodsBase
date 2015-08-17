/**
 * Contains classes for server that execute database queries
 * for multiple clients.
 */
package goodsbase.qserver;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Executes database tasks. Blocks DbTask object until the computation is done.
 * Notifies when task is complete. When thread is interrupted declines new
 * tasks. Finishes working when all tasks are complete.
 * 
 * @author Daria
 */
class QueryExecutor implements Runnable {

	/**
	 * @throws SQLException
	 *             when cannot connect to database
	 */
	public QueryExecutor(Connection conn) {
		connection = conn;
	}

	@Override
	public void run() {

		while (true) {
			try {
				execTask(queue.take());
			} catch (InterruptedException e) {
				/* stop accepting new tasks */
				interrupted = true;
				/* complete all remaining tasks */
				while (!queue.isEmpty()) {
					execTask(queue.remove());
				}
				/* stop service */
				log.info("Stopping query executor service...");
				try {
					connection.close();
				} catch (SQLException e1) {
					log.log(Level.WARNING,
							"Exception caught when closing connection", e1);
				}
				return;
			}
		}
	}

	/**
	 * Adds DbTask to the queue
	 * 
	 * @return true if task is accepted
	 */
	boolean addTask(DbTask task) {
		if (!interrupted)
			return queue.offer(task);
		else
			return false;
	}

	private void execTask(DbTask task) {
		synchronized (task) {
			task.execute(connection);
			/* notify the waiting thread */
			task.notify();
		}
	}

	private static final BlockingQueue<DbTask> queue = new LinkedBlockingQueue<DbTask>();
	private static final Logger log = Logger.getLogger(QueryExecutor.class
			.getName());
	private static Connection connection;
	private boolean interrupted = false;
}
