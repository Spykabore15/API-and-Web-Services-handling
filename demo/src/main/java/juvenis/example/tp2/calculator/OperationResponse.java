package juvenis.example.tp2.calculator;


import java.time.LocalDateTime;
public record OperationResponse(
    Long id,
    Double a,
    Double b,
    String operation,
    Double resultat,
    LocalDateTime date
) {
	
}