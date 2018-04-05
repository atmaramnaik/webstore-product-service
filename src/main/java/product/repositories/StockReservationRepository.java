package product.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import product.model.Product;
import product.model.StockReservation;
import product.model.StockTransaction;

import java.util.List;

public interface StockReservationRepository extends JpaRepository<StockReservation,Long> {
    List<StockReservation> findByProduct(Product product);
}
