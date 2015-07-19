package goodsbase.database;

/**Used to indicate database initialization errors*/
public class DbInitException extends Exception {

	private static final long serialVersionUID = 2395085451840341744L;

	public DbInitException() {		
	}

	public DbInitException(String message) {
		super(message);		
	}
	
	DbInitException(String message, Throwable cause) {
		super(message, cause);
	}
}
