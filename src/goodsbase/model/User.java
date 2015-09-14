package goodsbase.model;

import goodsbase.qserver.QRequest;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**Describes application user
 * @author Daria
 *
 */
public class User {
	
	public static final String ADMIN = "admin";
	public static final String WH_MANAGER = "whmanager";
	public static final String SAL_MANAGER = "salesmanager";

	/**
	 * @throws NoSuchAlgorithmException 
	 * 
	 */
	public User(String username, char[] password, String role) throws NoSuchAlgorithmException {
		this.setUsername(username);
		this.setPassword(password);
		this.setRole(role);
	}
	
	private User(){}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 * @throws NullPointerException if username is null
	 */
	public void setUsername(String username) {
		if(username == null) throw new NullPointerException("username can't be null");
		if(username.equals("")) throw new IllegalArgumentException("username can't be empty");
		this.username = username;
	}
	
	/**
	 * @param password the password to set. Will be overwritten after method invocation
	 * @throws NoSuchAlgorithmException if md5 algorithm is not available
	 */
	public void setPassword(char[] password) throws NoSuchAlgorithmException {
		try {
			this.password = toMd5Hex(password);
		} finally {
		Arrays.fill(password, '\u0000'); //clear sensitive data
		}
	}
	
	/**
	 * @return the role
	 */
	public String getRole() {
		return role;
	}

	/**
	 * @param role the role to set
	 * @throws NullPointerException if role is null
	 */
	public void setRole(String role) {
		if(role == null) throw new NullPointerException("role can't be null");
		this.role = role;
	}
	
	public static boolean createUser(User user) throws DataLoadException{
		String query = "INSERT INTO USERS (USERNAME, PASSWORD, ROLE) VALUES ('"+user.username
				+"', '"+ user.password +"', '"+user.role +"');";
		QRequest request = new QRequest(QRequest.Type.UPDATE);
		request.addQuery(query);
		int result = DataExecutor.execute(request);
		if (result == QRequest.OK_CODE)
			return true;
		return false;
	}
	
	public static boolean updateUser(User user) throws DataLoadException {
		String query = "UPDATE USERS SET username='"+user.username
				+ "', password ='" +user.password +"', role='"+user.role+"'"
						+ "where id="+user.id+");";
		QRequest request = new QRequest(QRequest.Type.UPDATE);
		request.addQuery(query);
		int result = DataExecutor.execute(request);
		if (result == QRequest.OK_CODE)
			return true;
		return false;
	}
	
	public static int getUsersCount() throws DataLoadException, XPathExpressionException {
		String query = "SELECT COUNT(*) AS NUM FROM users";
		QRequest request = new QRequest(QRequest.Type.SELECT);
		request.addQuery(query);
		Document doc;
		doc = DataExecutor.load(request);
		if (doc != null && doc.getChildNodes().getLength()>0) {
			XPathFactory fact = XPathFactory.newInstance();
			XPath xpath = fact.newXPath();
			String num = xpath.evaluate("result/line/NUM", doc);
			return Integer.valueOf(num);
		}			
		return -1;
	}
	
	public static User getUser(String username, char[] password) throws NoSuchAlgorithmException, DataLoadException, XPathExpressionException {
		String query = "SELECT * FROM USERS WHERE username='" + username
				+"' AND password='"+ toMd5Hex(password) +"';";
		QRequest req = new QRequest(QRequest.Type.SELECT);
		req.addQuery(query);
		try {
			Document doc = DataExecutor.load(req);
			if (doc != null && doc.getChildNodes().getLength()>0)  {
				XPathFactory fact = XPathFactory.newInstance();
				XPath xpath = fact.newXPath();
				Node n = (Node) xpath.evaluate("result/line", doc, XPathConstants.NODE);
				if(n!=null  && n.getChildNodes().getLength()>0)
					return parseUser(n, xpath);
			}
			return null;
		} finally {
			Arrays.fill(password, '\u0000');
		}
	}
	
	private static User parseUser(Node node, XPath xpath) throws NumberFormatException, XPathExpressionException {
		int id = Integer.valueOf(xpath.evaluate("USER_ID", node));
		String username = xpath.evaluate("USERNAME", node);
		String password = xpath.evaluate("PASSWORD", node);
		String role = xpath.evaluate("ROLE", node);
		User user = new User();
		user.id =id;
		user.setUsername(username);
		user.password = password;
		user.role = role;
		return user;
	}
	
	private static String toMd5Hex(char[] password) throws NoSuchAlgorithmException {
		byte[] pass = toBytes(password);
	    MessageDigest messageDigest = MessageDigest.getInstance("MD5");
	    messageDigest.reset();
	    messageDigest.update(pass);
	    byte[] digest = messageDigest.digest();
	    BigInteger integer = new BigInteger(digest);
	    return integer.toString(16);
	}
	
	private static byte[] toBytes(char[] chars) {
	    CharBuffer charBuffer = CharBuffer.wrap(chars);
	    ByteBuffer byteBuffer = Charset.forName("UTF-8").encode(charBuffer);
	    byte[] bytes = Arrays.copyOfRange(byteBuffer.array(),
	            byteBuffer.position(), byteBuffer.limit());
	    Arrays.fill(charBuffer.array(), '\u0000'); // clear sensitive data
	    Arrays.fill(byteBuffer.array(), (byte) 0); // clear sensitive data
	    return bytes;
	}

	private String username;
	private String password;
	private String role;
	private int id;
}
