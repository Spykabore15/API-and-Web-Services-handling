package juvenis.example.tp2.config;
import org.springframework.context.annotation.Bean; 

import org.springframework.context.annotation.Configuration; 

import org.springframework.web.reactive.function.client.WebClient; 

@Configuration 
public class WebClientConfig { 

    @Bean 
    public WebClient currencyWebClient() { 

        return WebClient.builder().baseUrl("https://api.frankfurter.dev/v1").build(); 

    } 

} 

 