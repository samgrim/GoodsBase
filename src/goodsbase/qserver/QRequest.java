package goodsbase.qserver;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
/**
 * Describes a database request. 
 * @author Daria
 *
 */
public class QRequest implements Serializable{
	
	/**SELECT returns result, UPDATE does not*/
	public enum Type implements Serializable{
		SELECT, UPDATE;
	}
	
	/**Creates a new request of specified type*/
	public QRequest(Type type) {
		this.type = type;
		this.body = new LinkedList<String>();
	}
	
	/**@return number of queries in the request*/
	public int getQueryNumber(){
		return body.size();
	}
	
	/**Adds query to request
	 * @return false if request doesn't allow more queries*/
	public boolean addQuery(String query){
		if(type == Type.SELECT&&getQueryNumber()==1)
			return false;
		body.add(query);
		return true;
	}
	
	/*Returns type of the query*/
	Type getType() {
		return type;
	}
	
	/*Returns queries*/
	List<String> getBody(){
		return body;
	}
	
	private Type type;
	private List<String> body;
	
	private static final long serialVersionUID = 931744542496010038L;
}
