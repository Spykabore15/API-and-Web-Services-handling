package juvenis.example.tp2.exception;

public class ManualConversionException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	 
	public ManualConversionException(String message) {
		super(message);
	}
	
	public ManualConversionException( 

            String message, 

            Throwable cause) { 

        super(message, cause); 

    } 
}
