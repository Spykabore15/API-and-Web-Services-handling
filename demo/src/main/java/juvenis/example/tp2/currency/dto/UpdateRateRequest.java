package juvenis.example.tp2.currency.dto; 

import jakarta.validation.constraints.DecimalMin; 

import jakarta.validation.constraints.NotNull; 

public class UpdateRateRequest { 

    @NotNull(message = "Le nouveau taux est obligatoire") 

    @DecimalMin( 

        value = "0.000001", 

        message = "Le taux doit être strictement positif" 

    ) 

    private Double rate; 

    public UpdateRateRequest() { 

    } 

    public Double getRate() { 

        return rate; 

    } 

    public void setRate(Double rate) { 

        this.rate = rate; 

    } 

} 