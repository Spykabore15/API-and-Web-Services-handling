 
package juvenis.example.tp2.currency; 

import java.util.List; 

import org.springframework.http.HttpStatus; 

import org.springframework.http.ResponseEntity; 

import org.springframework.web.bind.annotation.*; 


import jakarta.validation.Valid; 

import juvenis.example.tp2.currency.dto.CurrencyConversionResponse; 

import juvenis.example.tp2.currency.dto.ManualConversionRequest; 

import juvenis.example.tp2.currency.dto.UpdateRateRequest; 

@RestController 

@RequestMapping("/api/currency") 

public class CurrencyConverterController { 

    private final CurrencyService currencyService; 

    public CurrencyConverterController( 

            CurrencyService currencyService) { 

        this.currencyService = currencyService; 

    } 

    /* 

     * Conversion automatique utilisant l'API externe. 

     * 

     * Exemple : 

     * GET /api/currency/convert?amount=100&from=EUR&to=USD 

     */ 

    @GetMapping("/convert") 

    public ResponseEntity<CurrencyConversionResponse> convert( 

            @RequestParam Double amount, 

            @RequestParam String from, 

            @RequestParam String to) { 

        CurrencyConversionResponse response = 

                currencyService.convert( 

                        amount, 

                        from, 

                        to 

                ); 

        return ResponseEntity.ok(response); 

    } 

    /* 

     * Consultation de toutes les conversions enregistrées. 

     * 

     * Exemple : 

     * GET /api/currency/history 

     */ 

    @GetMapping("/history") 

    public ResponseEntity<List<CurrencyConversionResponse>> 

            getHistory() { 

        List<CurrencyConversionResponse> history = 

                currencyService.getHistory(); 

        return ResponseEntity.ok(history); 

    } 

    /* 

     * Conversion utilisant un taux fourni manuellement. 

     * 

     * Exemple : 

     * POST /api/currency/convert 

     */ 

    @PostMapping("/convert") 

    public ResponseEntity<CurrencyConversionResponse> 

            convertManually( 

                    @Valid 

                    @RequestBody 

                    ManualConversionRequest request) { 

        CurrencyConversionResponse response = 

                currencyService.convertManually(request); 

        return ResponseEntity 

                .status(HttpStatus.CREATED) 

                .body(response); 

    } 

    /* 

     * Modification du taux d'une transaction existante. 

     * 

     * Exemple : 

     * PUT /api/currency/1/rate 

     */ 

    @PutMapping("/{id}/rate") 

    public ResponseEntity<CurrencyConversionResponse> 

            updateRate( 

                    @PathVariable Long id, 

                    @Valid 

                    @RequestBody 

                    UpdateRateRequest request) { 

        CurrencyConversionResponse response = 

                currencyService.updateRate(id, request); 

        return ResponseEntity.ok(response); 

    } 

    /* 

     * Suppression d'une transaction existante. 

     * 

     * Exemple : 

     * DELETE /api/currency/1 

     */ 

    @DeleteMapping("/{id}") 

    public ResponseEntity<Void> deleteTransaction( 

            @PathVariable Long id) { 

        currencyService.deleteTransaction(id); 

        return ResponseEntity.noContent().build(); 

    } 

} 

 