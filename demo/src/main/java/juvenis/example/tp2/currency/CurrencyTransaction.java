package juvenis.example.tp2.currency;

import java.time.LocalDateTime; 

import jakarta.persistence.*; 

@Entity 

@Table(name = "currency_transactions") 

public class CurrencyTransaction { 

    @Id 

    @GeneratedValue(strategy = GenerationType.IDENTITY) 

    private Long id; 

    @Column(nullable = false) 

    private Double amount; 

    @Column(nullable = false, length = 3) 

    private String sourceCurrency; 

    @Column(nullable = false, length = 3) 

    private String targetCurrency; 

    @Column(nullable = false) 

    private Double rate; 

    @Column(nullable = false) 

    private Double convertedAmount; 

    @Column(nullable = false) 

    private LocalDateTime date; 

    public CurrencyTransaction() { 

    } 

    public CurrencyTransaction( 

            Double amount, 

            String sourceCurrency, 

            String targetCurrency, 

            Double rate, 

            Double convertedAmount) { 

        this.amount = amount; 

        this.sourceCurrency = sourceCurrency; 

        this.targetCurrency = targetCurrency; 

        this.rate = rate; 

        this.convertedAmount = convertedAmount; 

    } 

    @PrePersist 

    public void initializeDate() { 

        if (date == null) { 

            date = LocalDateTime.now(); 

        } 

    } 

    public Long getId() { 

        return id; 

    } 

    public Double getAmount() { 

        return amount; 

    } 

    public void setAmount(Double amount) { 

        this.amount = amount; 

    } 

    public String getSourceCurrency() { 

        return sourceCurrency; 

    } 

    public void setSourceCurrency(String sourceCurrency) { 

        this.sourceCurrency = sourceCurrency; 

    } 

    public String getTargetCurrency() { 

        return targetCurrency; 

    } 

    public void setTargetCurrency(String targetCurrency) { 

        this.targetCurrency = targetCurrency; 

    } 

    public Double getRate() { 

        return rate; 

    } 

    public void setRate(Double rate) { 

        this.rate = rate; 

    } 

    public Double getConvertedAmount() { 

        return convertedAmount; 

    } 

    public void setConvertedAmount( 

            Double convertedAmount) { 

        this.convertedAmount = convertedAmount; 

    } 

    public LocalDateTime getDate() { 

        return date; 

    } 

    public void setDate(LocalDateTime date) { 

        this.date = date; 

    } 

} 

 

 