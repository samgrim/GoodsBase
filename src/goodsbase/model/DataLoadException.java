package goodsbase.model;

/**Thrown when a problem occurs during loading or updating data 
 * @author Daria
 */
public class DataLoadException extends Exception {

	
	public DataLoadException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public DataLoadException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public DataLoadException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DataLoadException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}
	
	private static final long serialVersionUID = 7994415275515202662L;
}
