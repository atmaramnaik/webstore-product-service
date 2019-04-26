package product.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import product.model.Product;
import product.model.StockReservation;
import product.model.StockTransaction;
import product.model.StockTransactionType;
import product.repositories.StockReservationRepository;
import product.repositories.StockTransactionRepository;
import product.repositories.StockTransactionTypeRepository;

import java.math.BigDecimal;
import java.util.List;

@Service("stockManager")
public class StockManagerImpl implements StockManager{

    @Autowired
    private StockTransactionRepository stockTransactionRepository;

    @Autowired
    private StockTransactionTypeRepository stockTransactionTypeRepository;

    @Autowired
    private StockReservationRepository stockReservationRepository;

    public boolean purchase(Long productId,BigDecimal quantity) {
        stockTransaction(productId,quantity,stockTransactionTypeRepository.findByName("Purchase"));
        return true;
    }
    public boolean sold(Long productId,BigDecimal quantity) {
        stockTransaction(productId,quantity,stockTransactionTypeRepository.findByName("Sold"));
        reservation(productId,quantity,false);
        return true;
    }

    @Override
    public boolean book(Long productId, BigDecimal quantity) {
        reservation( productId, quantity,true);
        return false;
    }

    @Override
    public BigDecimal stock(Long productId) {
        Product product=new Product(productId);
        List<StockTransaction> stockTransactions=stockTransactionRepository.findByProduct(product);
        List<StockReservation> stockReservations=stockReservationRepository.findByProduct(product);

        return flattern(stockTransactions,stockReservations);
    }

    private boolean stockTransaction(Long productId, BigDecimal quantity, StockTransactionType stockTransactionType){
        StockTransaction stockTransaction=new StockTransaction();
        Product product=new Product(productId);
        stockTransaction.setProduct(product);
        stockTransaction.setQuantity(quantity);
        stockTransaction.setStockTransactionType(stockTransactionType);
        stockTransactionRepository.save(stockTransaction);
        return true;

    }
    private boolean reservation(Long productId, BigDecimal quantity,boolean reserve){
        StockReservation stockReservation=new StockReservation();
        Product product=new Product(productId);
        stockReservation.setProduct(product);
        stockReservation.setQuantity(quantity);
        stockReservation.setPositive(new Boolean(reserve));

        stockReservationRepository.save(stockReservation);
        return true;

    }
    private BigDecimal flattern(List<StockTransaction> stockTransactions,List<StockReservation> stockReservations){
        BigDecimal stock=stockTransactions
                .stream()
                .map(stockTransaction->{
                    return stockTransaction.getStockTransactionType().getPositive()?stockTransaction.getQuantity():(stockTransaction.getQuantity().multiply(new BigDecimal("-1")));})
                .reduce(BigDecimal.ZERO,(x,y)->x.add(y));
        BigDecimal reservation=stockReservations
                .stream()
                .map(stockReservation->{return stockReservation.getPositive()?stockReservation.getQuantity():(stockReservation.getQuantity().multiply(new BigDecimal("-1")));})
                .reduce(BigDecimal.ZERO,(x,y)->x.add(y));
        return stock.subtract(reservation);
    }
}
