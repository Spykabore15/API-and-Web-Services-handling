package juvenis.example.tp2.exception;

public class ZeroDivisionException extends RuntimeException {
	
    private static final long serialVersionUID = 1L;
    
    public ZeroDivisionException(String message) {
        super(message);
    }
}