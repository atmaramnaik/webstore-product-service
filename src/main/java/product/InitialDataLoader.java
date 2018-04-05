package product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import product.model.Product;
import product.model.StockTransaction;
import product.model.StockTransactionType;
import product.repositories.ProductRepository;
import product.repositories.StockReservationRepository;
import product.repositories.StockTransactionRepository;
import product.repositories.StockTransactionTypeRepository;

import java.math.BigDecimal;

@Component
public class InitialDataLoader implements
        ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    @Autowired
    private StockTransactionTypeRepository stockTransactionTypeRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockTransactionRepository stockTransactionRepository;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup)
            return;
        StockTransactionType stockTransactionType=createStockTransactionTypeIfNotFound("Purchase",true);
        createStockTransactionTypeIfNotFound("Sold",false);
        Product product1=createProductIfNotFound("Nescafe 100g",BigDecimal.valueOf(100.0));
        Product product2=createProductIfNotFound("Tata Tea 100g",BigDecimal.valueOf(120.0));
        Product product3=createProductIfNotFound("Cadburies Dairymilk 10g",BigDecimal.valueOf(10.0));
        createStockTransactionIfNotFound(product1,BigDecimal.valueOf(10),stockTransactionType);
        createStockTransactionIfNotFound(product2,BigDecimal.valueOf(10),stockTransactionType);
        createStockTransactionIfNotFound(product3,BigDecimal.valueOf(10),stockTransactionType);
        alreadySetup = true;
    }

    @Transactional
    public StockTransactionType createStockTransactionTypeIfNotFound(String name,boolean isPositive) {
        StockTransactionType type=new StockTransactionType();
        type.setName(name);
        type.setPositive(isPositive);

        StockTransactionType stockTransactionType = stockTransactionTypeRepository.findByName (type.getName());
        if (stockTransactionType == null) {
            stockTransactionType=stockTransactionTypeRepository.save(type);
        }
        return stockTransactionType;
    }
    @Transactional
    public Product createProductIfNotFound(String name, BigDecimal price) {
        Product product=new Product();
        product.setName(name);
        product.setPrice(price);

        Product product1 = productRepository.findByName(name);
        if (product1 == null) {
            product1=productRepository.save(product);
        }
        return product1;
    }
    @Transactional
    public StockTransaction createStockTransactionIfNotFound(Product product,BigDecimal quantity,StockTransactionType stockTransactionType) {
        StockTransaction stockTransaction=new StockTransaction();
        stockTransaction.setProduct(product);
        stockTransaction.setQuantity(quantity);
        stockTransaction.setStockTransactionType(stockTransactionType);
        StockTransaction stockTransaction1 = stockTransactionRepository.findByProductAndStockTransactionType(product,stockTransactionType);
        if (stockTransaction1 == null) {
            stockTransaction1=stockTransactionRepository.save(stockTransaction);
        }
        return stockTransaction1;
    }
}
