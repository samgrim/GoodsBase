
package goodsbase.qserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**Communicates with client on socket,
 * confirms and sends queries for execution,
 * handles query results. 
 * 
 * @author Daria
 *
 */
class QueryHandler implements Runnable {

	/**
	 * @param socket - connected socket
	 */
	public QueryHandler(Socket socket, QueryExecutor executor) {
		this.socket = socket;
		this.executor = executor;
	}

	@Override
	public void run() {
		QRequest request = null;
		try {
			request = getRequest();
		} catch (IOException e) {
			log.log(Level.WARNING,"Exception caught during request processing", e);
		}
		if (request != null) {
			switch (request.getType()){
			case SELECT:
				processSelect(request);
				break;
			default:
				break;
			}
		}
		try {
			socket.close();
		} catch (IOException e) {
			log.log(Level.WARNING,"Exception caught during socket closing", e);
		}
	}
	
	private void processSelect(QRequest request) {
		QueryTask task = new QueryTask(request);
		try (PrintWriter out = new PrintWriter(socket.getOutputStream());) {	// out will be closed automatically
			executeTask(task);
			if(task.getExceptions().size() == 0) {
				out.write(String.valueOf(QRequest.OK_CODE));
				out.write("\n");
				out.flush();
				//TODO: response must be ready before writing
				try {
					writeResponse(task.getResult(), socket.getOutputStream());
				} catch (XMLStreamException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				out.write(String.valueOf(QRequest.ERROR_CODE));
				out.write("\n");
				out.flush();
			}
		} catch (IOException e) {
			log.log(Level.WARNING,"Exception caught during request processing", e);
		} 
	}
	
	private void executeTask(DbTask task){
		executor.addTask(task);
		synchronized(task) {
			while(!task.isComplete())
				try {
					task.wait();
				} catch (InterruptedException e) {
					log.log(Level.WARNING,"Something has interrupted waiting for task result", e);
				}
		}
	}

	private QRequest getRequest() throws IOException {
		ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
		try {
			return (QRequest) in.readObject();
		} catch (ClassNotFoundException e) {
			return null;
		}
	}
	
	/*writes Result set as xml file*/
	private static void writeResponse(ResultSet set, OutputStream out) throws XMLStreamException, SQLException {
		XMLOutputFactory factory = XMLOutputFactory.newInstance();	
		XMLStreamWriter writer = null;	
		ResultSetMetaData metaData = set.getMetaData();
		int colsCount = metaData.getColumnCount();
		try {
			writer = factory.createXMLStreamWriter(out, "utf-8");	//!!important to set utf-8 directly				
			writer.writeStartDocument();
			writer.writeStartElement("result");
			while(set.next()) {
				writer.writeStartElement("line");
				for(int i = 1; i <= colsCount; i++) {
					writer.writeStartElement(metaData.getColumnLabel(i));
					writer.writeCharacters(set.getString(i));
					writer.writeEndElement();
				}
				writer.writeEndElement();
			}
			writer.writeEndDocument(); //closes "result"
		} finally {		// close writer
			if(writer != null)
				writer.close();
		}
	}
	
	private Socket socket;
	private QueryExecutor executor;
	private static final Logger log = Logger.getLogger(QueryHandler.class.getName());
}
