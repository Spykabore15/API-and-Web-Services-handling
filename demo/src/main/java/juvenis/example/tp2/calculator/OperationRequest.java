package juvenis.example.tp2.calculator;

import jakarta.validation.constraints.*;

public class OperationRequest {
    @NotNull(message = "Le premier nombre a est obligatoire")
    @DecimalMin(
        value = "-1000000.0",
        message = "Le nombre a doit être supérieur ou égal à -1000000"
    )
    @DecimalMax(
        value = "1000000.0",
        message = "Le nombre a doit être inférieur ou égal à 1000000"
    )
    private Double a;
    @NotNull(message = "Le deuxième nombre b est obligatoire")
    @DecimalMin(
        value = "-1000000.0",
        message = "Le nombre b doit être supérieur ou égal à -1000000"
    )
    @DecimalMax(
        value = "1000000.0",
        message = "Le nombre b doit être inférieur ou égal à 1000000"
    )
    private Double b;
    @NotBlank(message = "L'opération est obligatoire")
    @Pattern(
        regexp = "addition|soustraction|multiplication|division",
        flags = Pattern.Flag.CASE_INSENSITIVE,
        message = "L'opération doit être addition, soustraction, multiplication ou division"
    )
    private String operation;
    public OperationRequest() {
    }
    public Double getA() {
        return a;
    }
    public void setA(Double a) {
        this.a = a;
    }
    public Double getB() {
        return b;
    }
    public void setB(Double b) {
        this.b = b;
    }
    public String getOperation() {
        return operation;
    }
    public void setOperation(String operation) {
        this.operation = operation;
    }
}
