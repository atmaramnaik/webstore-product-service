package product.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import product.model.Product;
import product.model.StockTransaction;
import product.model.StockTransactionType;

import java.util.List;

public interface StockTransactionRepository extends JpaRepository<StockTransaction,Long> {
    List<StockTransaction> findByProduct(Product product);
    StockTransaction findByProductAndStockTransactionType(Product product, StockTransactionType stockTransactionType);
}
