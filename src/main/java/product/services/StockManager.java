package product.services;

import product.model.StockTransaction;

import java.math.BigDecimal;

public interface StockManager {
    public boolean purchase(Long productId,BigDecimal quantity);
    public boolean sold(Long productId,BigDecimal quantity);
    public boolean book(Long productId,BigDecimal quantity);
    public BigDecimal stock(Long productId);
}
