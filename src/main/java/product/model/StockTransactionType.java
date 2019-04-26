package product.model;

import javax.persistence.*;

@Entity
@Table(name = "stock_transaction_types")
public class StockTransactionType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private Boolean isPositive;

    @Column
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getPositive() {
        return isPositive;
    }

    public void setPositive(Boolean positive) {
        isPositive = positive;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {

        // If the object is compared with itself then return true
        if (o == this) {
            return true;
        }

        /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
        if (!(o instanceof StockTransactionType)) {
            return false;
        }

        // typecast o to Complex so that we can compare data members
        StockTransactionType c = (StockTransactionType) o;

        // Compare the data members and return accordingly
        boolean idComp=true;
        if(this.id!=null && c.id!=null){
            idComp=this.id.equals(c.id);
        }
        return idComp && this.getName().equals(c.getName()) && this.getPositive().equals(c.getPositive());
    }
}
