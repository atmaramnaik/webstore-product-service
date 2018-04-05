package product.model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "stock_reservations")
public class StockReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(cascade = CascadeType.DETACH)
    private Product product;

    @Column
    private Boolean isPositive;

    @Column
    private BigDecimal quantity;

    @Column
    @CreationTimestamp
    private Timestamp createDateTime;

    @Column
    @UpdateTimestamp
    private Timestamp updateDateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Boolean getPositive() {
        return isPositive;
    }

    public void setPositive(Boolean positive) {
        isPositive = positive;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Timestamp getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(Timestamp createDateTime) {
        this.createDateTime = createDateTime;
    }

    public Timestamp getUpdateDateTime() {
        return updateDateTime;
    }

    public void setUpdateDateTime(Timestamp updateDateTime) {
        this.updateDateTime = updateDateTime;
    }

    @Override
    public boolean equals(Object o) {

        // If the object is compared with itself then return true
        if (o == this) {
            return true;
        }

        /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
        if (!(o instanceof StockReservation)) {
            return false;
        }

        // typecast o to Complex so that we can compare data members
        StockReservation c = (StockReservation) o;

        // Compare the data members and return accordingly
        boolean idComp=true;
        if(this.id!=null && c.id!=null){
            idComp=this.id.equals(c.id);
        }
        return idComp && this.getProduct().getId().equals(c.getProduct().getId()) && this.getQuantity().equals(c.getQuantity()) && this.getPositive().equals(c.getPositive());
    }
}
