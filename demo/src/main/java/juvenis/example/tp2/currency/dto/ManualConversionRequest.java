package juvenis.example.tp2.currency.dto; 

import jakarta.validation.constraints.DecimalMin; 

import jakarta.validation.constraints.NotBlank; 

import jakarta.validation.constraints.NotNull; 

import jakarta.validation.constraints.Pattern; 

public class ManualConversionRequest { 

    @NotNull(message = "Le montant est obligatoire") 

    @DecimalMin( 

        value = "0.01", 

        message = "Le montant doit être strictement positif" 

    ) 

    private Double amount; 

    @NotBlank(message = "La devise source est obligatoire") 

    @Pattern( 

        regexp = "[A-Za-z]{3}", 

        message = "La devise source doit contenir trois lettres" 

    ) 

    private String from; 

    @NotBlank(message = "La devise cible est obligatoire") 

    @Pattern( 

        regexp = "[A-Za-z]{3}", 

        message = "La devise cible doit contenir trois lettres" 

    ) 

    private String to; 

    @NotNull(message = "Le taux est obligatoire") 

    @DecimalMin( 

        value = "0.000001", 

        message = "Le taux doit être strictement positif" 

    ) 

    private Double rate; 

    public ManualConversionRequest() { 

    } 

    public Double getAmount() { 

        return amount; 

    } 

    public void setAmount(Double amount) { 

        this.amount = amount; 

    } 

    public String getFrom() { 

        return from; 

    } 

    public void setFrom(String from) { 

        this.from = from; 

    } 

    public String getTo() { 

        return to; 

    } 

    public void setTo(String to) { 

        this.to = to; 

    } 

    public Double getRate() { 

        return rate; 

    } 

    public void setRate(Double rate) { 

        this.rate = rate; 

    } 

} 

 