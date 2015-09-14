package goodsbase.model;

import goodsbase.qserver.QRequest;
import goodsbase.qserver.QServer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class DataExecutor {
	
	
	/**Loads data from the server as an xml document
	 * @throws DataLoadException*/
	public static Document load(QRequest req) throws DataLoadException  {
		Document doc = null;
		try(Socket s = new Socket(HOST, PORT);){
			ObjectOutputStream out  = new ObjectOutputStream(s.getOutputStream());
			out.writeObject(req);
			Scanner in = new Scanner(s.getInputStream());
			int result = Integer.valueOf(in.nextLine());
			if(result == QRequest.OK_CODE) {
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				doc = builder.parse(s.getInputStream());
				in.close();
			}} catch (IOException | ParserConfigurationException | SAXException e) {
				throw new DataLoadException(e);
			} 
		return doc;
	}
	
	/**Sends request to the server and receives response code
	 * @throws DataLoadException */
	public static int execute(QRequest req) throws DataLoadException {
		int result = 0;
		try(Socket s = new Socket(HOST, PORT);){
			ObjectOutputStream out  = new ObjectOutputStream(s.getOutputStream());
			out.writeObject(req);
			Scanner in = new Scanner(s.getInputStream());
			result = in.nextInt();
			in.close();
		} catch (IOException e) {
			throw new DataLoadException(e);
		}
		return result;
	}
	
	//TODO: make property
	private static final int PORT = QServer.PORT;
	private static final String HOST = "localhost";
}
