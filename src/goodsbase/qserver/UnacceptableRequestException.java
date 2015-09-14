package goodsbase.qserver;

/**
 * To be thrown when QRequest doesn't suite to DBTask
 * 
 * @author Daria
 * 
 */
public class UnacceptableRequestException extends IllegalArgumentException {

	public UnacceptableRequestException(String s) {
		super(s);
	}

	public UnacceptableRequestException(Throwable cause) {
		super(cause);

	}

	public UnacceptableRequestException(String message, Throwable cause) {
		super(message, cause);
	}

	private static final long serialVersionUID = -2445191333680914374L;

}
