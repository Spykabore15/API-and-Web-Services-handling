 

package juvenis.example.tp2.currency; 

import java.util.List; 

import org.springframework.stereotype.Service; 

import org.springframework.transaction.annotation.Transactional; 

import juvenis.example.tp2.currency.dto.CurrencyConversionResponse; 

import juvenis.example.tp2.currency.dto.ManualConversionRequest; 

import juvenis.example.tp2.currency.dto.UpdateRateRequest;
import juvenis.example.tp2.exception.ManualConversionException;
import juvenis.example.tp2.exception.ResourceNotFoundException; 

@Service 

public class CurrencyService { 

    private final CurrencyRateClient currencyRateClient; 

    private final CurrencyTransactionRepository transactionRepository; 

    public CurrencyService( 

            CurrencyRateClient currencyRateClient, 

            CurrencyTransactionRepository transactionRepository) { 

        this.currencyRateClient = currencyRateClient; 

        this.transactionRepository = transactionRepository; 

    } 

    /* 

     * Conversion automatique. 

     * 

     * Le taux est récupéré auprès de l'API externe 

     * par CurrencyRateClient. 

     */ 

    @Transactional 

    public CurrencyConversionResponse convert( 

            Double amount, 

            String from, 

            String to) { 

        validateAmount(amount); 

        String normalizedFrom = normalizeCurrency(from); 

        String normalizedTo = normalizeCurrency(to); 

        validateDifferentCurrencies( 

                normalizedFrom, 

                normalizedTo 

        ); 

        Double rate = currencyRateClient.getRate( 

                normalizedFrom, 

                normalizedTo 

        ); 

        Double convertedAmount = amount * rate; 

        CurrencyTransaction transaction = 

                new CurrencyTransaction( 

                        amount, 

                        normalizedFrom, 

                        normalizedTo, 

                        rate, 

                        convertedAmount 

                ); 

        CurrencyTransaction savedTransaction = 

                transactionRepository.save(transaction); 

        return toResponse(savedTransaction); 

    } 

    /* 

     * Conversion manuelle. 

     * 

     * Le taux est fourni dans le JSON envoyé par le client. 

     * Aucun appel à l'API externe n'est effectué. 

     */ 

    @Transactional 

    public CurrencyConversionResponse convertManually( 

            ManualConversionRequest request) { 

        validateAmount(request.getAmount()); 

        validateRate(request.getRate()); 

        String normalizedFrom = 

                normalizeCurrency(request.getFrom()); 

        String normalizedTo = 

                normalizeCurrency(request.getTo()); 

        validateDifferentCurrencies( 

                normalizedFrom, 

                normalizedTo 

        ); 

        Double convertedAmount = 

                request.getAmount() * request.getRate(); 

        CurrencyTransaction transaction = 

                new CurrencyTransaction( 

                        request.getAmount(), 

                        normalizedFrom, 

                        normalizedTo, 

                        request.getRate(), 

                        convertedAmount 

                ); 

        CurrencyTransaction savedTransaction = 

                transactionRepository.save(transaction); 

        return toResponse(savedTransaction); 

    } 

    /* 

     * Consultation de toutes les conversions enregistrées. 

     */ 

    @Transactional(readOnly = true) 

    public List<CurrencyConversionResponse> getHistory() { 

        return transactionRepository 

                .findAll() 

                .stream() 

                .map(this::toResponse) 

                .toList(); 

    } 

    /* 

     * Consultation d'une transaction à partir de son identifiant. 

     */ 

    @Transactional(readOnly = true) 

    public CurrencyConversionResponse getTransactionById( 

            Long id) { 

        CurrencyTransaction transaction = 

                findTransaction(id); 

        return toResponse(transaction); 

    } 

    /* 

     * Modification du taux d'une transaction existante. 

     * 

     * Le montant converti doit obligatoirement être recalculé. 

     */ 

    @Transactional 

    public CurrencyConversionResponse updateRate( 

            Long id, 

            UpdateRateRequest request) { 

        validateRate(request.getRate()); 

        CurrencyTransaction transaction = 

                findTransaction(id); 

        Double newRate = request.getRate(); 

        Double newConvertedAmount = 

                transaction.getAmount() * newRate; 

        transaction.setRate(newRate); 

        transaction.setConvertedAmount( 

                newConvertedAmount 

        ); 

        CurrencyTransaction updatedTransaction = 

                transactionRepository.save(transaction); 

        return toResponse(updatedTransaction); 

    } 

    /* 

     * Suppression d'une transaction. 

     */ 

    @Transactional 

    public void deleteTransaction(Long id) { 

        CurrencyTransaction transaction = 

                findTransaction(id); 

        transactionRepository.delete(transaction); 

    } 

    /* 

     * Recherche interne d'une transaction. 

     * 

     * Si la transaction n'existe pas, 

     * une exception 404 sera déclenchée. 

     */ 

    private CurrencyTransaction findTransaction(Long id) { 

        if (id == null) { 

            throw new IllegalArgumentException( 

                    "L'identifiant de la transaction est obligatoire" 

            ); 

        } 

        return transactionRepository 

                .findById(id) 

                .orElseThrow(() -> 

                        new ResourceNotFoundException( 

                                "Aucune transaction trouvée " 

                                + "avec l'identifiant " + id 

                        ) 

                ); 

    } 

    /* 

     * Vérification du montant initial. 

     */ 

    private void validateAmount(Double amount) { 

        if (amount == null) { 

            throw new IllegalArgumentException( 

                    "Le montant est obligatoire" 

            ); 

        } 

        if (!Double.isFinite(amount)) { 

            throw new IllegalArgumentException( 

                    "Le montant doit être un nombre valide" 

            ); 

        } 

        if (amount <= 0) { 

            throw new IllegalArgumentException( 

                    "Le montant doit être strictement positif" 

            ); 

        } 

    } 

    /* 

     * Vérification d'un taux manuel ou mis à jour. 

     */ 

    private void validateRate(Double rate) { 

        if (rate == null) { 

            throw new IllegalArgumentException( 

                    "Le taux est obligatoire" 

            ); 

        } 

        if (!Double.isFinite(rate)) { 

            throw new IllegalArgumentException( 

                    "Le taux doit être un nombre valide" 

            ); 

        } 

        if (rate <= 0) { 

            throw new IllegalArgumentException( 

                    "Le taux doit être strictement positif" 

            ); 

        } 

    } 

    /* 

     * Normalisation et validation du code de devise. 
     * Exemple : 
     * " eur " devient "EUR".
     */ 

    private String normalizeCurrency( 

            String currency) { 

        if (currency == null 

                || currency.isBlank()) { 

            throw new IllegalArgumentException( 

                    "Le code de devise est obligatoire" 

            ); 

        } 

        String normalizedCurrency = 

                currency.trim().toUpperCase(); 

        if (!normalizedCurrency.matches("[A-Z]{3}")) { 

            throw new IllegalArgumentException( 

                    "Le code de devise doit contenir " 

                    + "exactement trois lettres" 

            ); 

        } 

        return normalizedCurrency; 

    } 

    /* 

     * Vérification de la paire de devises. 

     */ 

    private void validateDifferentCurrencies( 

            String from, 

            String to) { 

        if (from.equals(to)) { 

            throw new IllegalArgumentException( 

                    "La devise source et la devise cible " 

                    + "doivent être différentes" 

            ); 

        } 

    } 

    /* 

     * Transformation de l'entité JPA 

     * en DTO de réponse JSON. 

     */ 

    private CurrencyConversionResponse toResponse(CurrencyTransaction transaction) { 

        return new CurrencyConversionResponse( 

                transaction.getId(), 

                transaction.getAmount(), 

                transaction.getSourceCurrency(), 

                transaction.getTargetCurrency(), 

                transaction.getRate(), 

                transaction.getConvertedAmount(), 

                transaction.getDate() 

        ); 

    } 

} 

 