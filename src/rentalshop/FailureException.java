package rentalshop;

/**
 * This exception indicates service failure.
 *  
 * @author Petr Adamek
 */
public class FailureException extends RuntimeException {
    public FailureException(String msg) {
        super(msg);
    }

    public FailureException(Throwable cause) {
        super(cause);
    }

    public FailureException(String message, Throwable cause) {
        super(message, cause);
    }

}
