package product.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import product.model.Product;
import product.model.StockTransaction;
import product.model.StockTransactionType;

public interface StockTransactionTypeRepository extends JpaRepository<StockTransactionType,Long> {
    StockTransactionType findByName(String name);
}
