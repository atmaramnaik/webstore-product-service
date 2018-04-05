package product.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import product.model.Product;

public interface ProductRepository extends JpaRepository<Product,Long> {
    Product findByName(String name);
}
