package product.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class OrderDTO implements Serializable{
    private BigDecimal quantity;

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }
}
