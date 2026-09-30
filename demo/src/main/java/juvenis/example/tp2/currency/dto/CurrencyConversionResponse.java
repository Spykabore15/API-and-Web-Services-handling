package juvenis.example.tp2.currency.dto;

import java.time.LocalDateTime;

public record CurrencyConversionResponse( 

	Long id, 
	
	Double amount, 
	
	String from, 
	
	String to, 
	
	Double rate, 
	
	Double convertedAmount, 
	
	LocalDateTime date) {
}

 