package exception;

public class PayrollServiceException extends Exception {

    private static final long serialVersionUID = 1L;

    public PayrollServiceException(String message) {
        super(message);
    }

    public PayrollServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
