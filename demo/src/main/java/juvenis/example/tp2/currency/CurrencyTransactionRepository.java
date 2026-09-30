package juvenis.example.tp2.currency;


import org.springframework.data.jpa.repository.JpaRepository; 

import org.springframework.stereotype.Repository; 

@Repository 

public interface CurrencyTransactionRepository 
        extends JpaRepository<CurrencyTransaction, Long> { 

} 

 