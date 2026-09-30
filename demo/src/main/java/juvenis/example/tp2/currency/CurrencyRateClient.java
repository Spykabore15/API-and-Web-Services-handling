package juvenis.example.tp2.currency;


import org.springframework.stereotype.Component; 


import org.springframework.web.reactive.function.client.WebClient; 

import org.springframework.web.reactive.function.client.WebClientException;

import org.springframework.cache.annotation.Cacheable; 
import org.springframework.retry.annotation.Backoff; 
import org.springframework.retry.annotation.Retryable; 

import juvenis.example.tp2.currency.dto.ExternalRateResponse; 

import juvenis.example.tp2.exception.ExternalServiceException; 

@Component 

public class CurrencyRateClient { 

    private final WebClient currencyWebClient; 

    public CurrencyRateClient( 

            WebClient currencyWebClient) { 

        this.currencyWebClient = currencyWebClient; 

    } 
    
    @Retryable(
		retryFor = ExternalServiceException.class,
		maxAttempts = 3, 
		backoff = @Backoff(delay = 1000) 
    ) 
    @Cacheable(
		value ="currencyRates",
		key ="#from + '-' + #to"
    )
    public Double getRate( 
            String from, 
            String to) {
    	
    	System.out.println("Appel de l'API externe pour " + from + " vers " + to);

    			 
        try { 

            ExternalRateResponse response = 

                    currencyWebClient 

                        .get() 
                        .uri("/latest?from={from}&to={to}", from, to) 

                        .retrieve() 

                        .bodyToMono(ExternalRateResponse.class) 

                        .block(); 

            if (response == null 

                    || response.getRates() == null 

                    || !response.getRates().containsKey(to)) { 

                throw new ExternalServiceException( 

                    "Aucun taux disponible pour " 

                    + from + " vers " + to 

                ); 

            } 

            return response.getRates().get(to); 

        } catch (ExternalServiceException exception) { 

            throw exception; 

        } catch (WebClientException exception) { 

            throw new ExternalServiceException( 

                "Le service externe de taux de change " 

                + "est indisponible", 

                exception 

            ); 

        } 

    } 

} 

 