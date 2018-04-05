package product.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import product.model.Product;
import product.repositories.ProductRepository;

import java.util.List;

@Service("productManager")
public class ProductManagerImpl implements ProductManager {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Override
    public Product findById(Long id) {
        return productRepository.findOne(id);
    }

    @Override
    public Product create(Product product) {
        return productRepository.save(product);
    }
}
