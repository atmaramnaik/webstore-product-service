package product.services;

import product.model.Product;

import java.util.List;

public interface ProductManager {
    public List<Product> getAll();
    public Product findById(Long id);
    public Product create(Product product);
}
