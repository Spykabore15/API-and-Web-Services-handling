package juvenis.example.tp2.calculator;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "operations")
public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Double a;
    @Column(nullable = false)
    private Double b;
    @Column(nullable = false)
    private String operateur;
    @Column(nullable = false)
    private Double resultat;
    @Column(nullable = false)
    private LocalDateTime date;
    public Operation() {
    }
    public Operation(
            Double a,
            Double b,
            String operateur,
            Double resultat) {
        this.a = a;
        this.b = b;
        this.operateur = operateur;
        this.resultat = resultat;
    }
    @PrePersist
    public void initialiserDate() {
        if (date == null) {
            date = LocalDateTime.now();
        }
    }
    public Long getId() {
        return id;
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
    public String getOperateur() {
        return operateur;
    }
    public void setOperateur(String operateur) {
        this.operateur = operateur;
    }
    public Double getResultat() {
        return resultat;
    }
    public void setResultat(Double resultat) {
        this.resultat = resultat;
    }
    public LocalDateTime getDate() {
        return date;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}