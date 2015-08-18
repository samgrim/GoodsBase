package goodsbase.qserver;

import goodsbase.database.DbConnection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**Query Server main class.
 * @author Daria
 */
public class QServer implements Runnable{
	
	//TODO:remove
	public static void main(String[] args) throws Exception {
		QServer.start();
		Thread.sleep(10000);
		QServer.stop();
	}

	@Override
	public void run() {		
		while (!Thread.interrupted()) {
			try {
				Socket sock = serverSocket.accept();
				QueryHandler handler  = new QueryHandler(sock, queryExecutor);
				clientPool.execute(handler);				
			} catch(SocketException e0) {
				log.info("Closing waiting socket");
			} catch (IOException e) {
				log.log(Level.WARNING, "Exception caught ", e);
			}
			
		}
	}
	/** Starts the server
	 * @throws Exception
	 */
	public static void start() throws Exception{
		if (mainThread!=null && mainThread.isAlive()) {
			log.info("Server is already alive");
		} else {
			log.info("Server is starting...");			
				
				try {
					log.info("Initializing database");
					DbConnection.init();
					log.info("Getting connection");
					dbConnection = DbConnection.getConnection();
					log.info("Launching query executor service");
					queryExecutor = new QueryExecutor(dbConnection);
					queryExecThread = new Thread(queryExecutor);
					queryExecThread.start();
					log.info("Creating thread pool for clients requests");
					clientPool = Executors.newCachedThreadPool();
					log.info("Binding to the port " + PORT);
					try{
						serverSocket = new ServerSocket(PORT);
					} catch (IOException e0) {
						log.log(Level.WARNING, "Failed to bind on port " + PORT, e0);
						log.info("Stopping services");
						try {
							dbConnection.close();
						} catch (SQLException e1) {
							log.log(Level.WARNING, "Exception caught when closing db connection", e1);
						}
						queryExecThread.interrupt();
						queryExecThread.join();
						clientPool.shutdown();
						log.info("Server stopped");
						throw e0;
					}
					mainThread = new Thread(new QServer());
					mainThread.start();
					log.info("Server is running");
				} catch (Exception e) {
					log.log(Level.SEVERE, "Failed to start server", e);
					throw e;
				}
		}
	}
	/**Stops the server*/
	public static void stop() throws InterruptedException{
		log.info("Stopping server");
		clientPool.shutdown();
		mainThread.interrupt();
		log.info("Stopping services");
		queryExecThread.interrupt();
		queryExecThread.join();	
		log.info("Closing db connection");
		try {
			dbConnection.close();
		} catch (SQLException e1) {
			log.log(Level.WARNING, "Exception caught when closing db connection", e1);
		}
		log.info("Closing server socket");
		try {
			serverSocket.close();
		} catch (IOException e) {
			log.log(Level.WARNING, "Exception caught when closing server socket", e);
		}
		mainThread.join();		
		log.info("Server stopped");
	}
	
	public static boolean isAlive() {
		if(mainThread != null)
			return mainThread.isAlive();
		return false;
	}
	
	private static QueryExecutor queryExecutor;
	private static Thread mainThread;
	private static Thread queryExecThread;
	private static ServerSocket serverSocket;
	private static ExecutorService clientPool;
	private static Connection dbConnection;
	private static final Logger log =Logger.getLogger(QServer.class.getName());	
	
	//TODO: make properties
	public static final int PORT = 8382;
}
