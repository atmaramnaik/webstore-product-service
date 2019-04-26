package product.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import product.model.Product;
import product.model.StockReservation;
import product.model.StockTransaction;
import product.model.StockTransactionType;
import product.repositories.StockReservationRepository;
import product.repositories.StockTransactionRepository;
import product.repositories.StockTransactionTypeRepository;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;

@RunWith(SpringRunner.class)
public class StockManagerTest {
    @TestConfiguration
    static class StockManagerTestContextConfiguration {

        @Bean
        public StockManager stockManager() {
            return new StockManagerImpl();
        }
    }
    @Autowired
    private StockManager stockManager;

    @MockBean
    private StockTransactionRepository stockTransactionRepository;

    @MockBean
    private StockTransactionTypeRepository stockTransactionTypeRepository;

    @MockBean
    private StockReservationRepository stockReservationRepository;

    @Test
    public void should_put_purchase_stock_transaction_on_purchase(){
        StockTransactionType stockTransactionType=new StockTransactionType();
        stockTransactionType.setName("Purchase");
        stockTransactionType.setPositive(new Boolean(true));
        stockTransactionType.setId(new Long("1"));
        doReturn(stockTransactionType).when(stockTransactionTypeRepository).findByName("Purchase");

        StockTransaction stockTransaction=new StockTransaction();

        Product product=new Product(new Long(1));
        stockTransaction.setProduct(product);
        stockTransaction.setQuantity(BigDecimal.valueOf(10.10));
        stockTransaction.setStockTransactionType(stockTransactionType);

        stockManager.purchase(new Long(1), BigDecimal.valueOf(10.10));

        verify(stockTransactionRepository,times(1)).save(eq(stockTransaction));

    }
    @Test
    public void should_put_sold_stock_transaction_on_sold_and_unreserve(){
        StockTransactionType stockTransactionType=new StockTransactionType();
        stockTransactionType.setName("Sold");
        stockTransactionType.setPositive(new Boolean(false));
        stockTransactionType.setId(new Long("1"));
        doReturn(stockTransactionType).when(stockTransactionTypeRepository).findByName("Sold");

        StockTransaction stockTransaction=new StockTransaction();

        Product product=new Product(new Long(1));
        stockTransaction.setProduct(product);
        stockTransaction.setQuantity(BigDecimal.valueOf(10.10));
        stockTransaction.setStockTransactionType(stockTransactionType);

        StockReservation stockReservation=new StockReservation();
        stockReservation.setProduct(product);
        stockReservation.setQuantity(BigDecimal.valueOf(10.10));
        stockReservation.setPositive(new Boolean(false));

        stockManager.sold(new Long(1), BigDecimal.valueOf(10.10));



        verify(stockTransactionRepository,times(1)).save(eq(stockTransaction));
        verify(stockReservationRepository,times(1)).save(eq(stockReservation));

    }

    @Test
    public void should_put_reservation_on_book(){
        StockTransactionType stockTransactionType=new StockTransactionType();
        stockTransactionType.setName("Sold");
        stockTransactionType.setPositive(new Boolean(false));
        stockTransactionType.setId(new Long("1"));
        doReturn(stockTransactionType).when(stockTransactionTypeRepository).findByName("Sold");

        StockReservation stockReservation=new StockReservation();

        Product product=new Product(new Long(1));
        stockReservation.setProduct(product);
        stockReservation.setQuantity(BigDecimal.valueOf(10.10));
        stockReservation.setPositive(new Boolean(true));

        stockManager.book(new Long(1), BigDecimal.valueOf(10.10));

        verify(stockReservationRepository,times(1)).save(eq(stockReservation));
    }

    @Test
    public void should_return_zero_stock_if_there_is_no_stock_and_reservation(){


        doReturn(new ArrayList<StockTransaction>()).when(stockTransactionRepository).findByProduct(any(Product.class));
        doReturn(new ArrayList<StockReservation>()).when(stockReservationRepository).findByProduct(any(Product.class));


        BigDecimal resp=stockManager.stock(new Long(1));
        assertEquals(resp,BigDecimal.valueOf(0));

    }
    @Test
    public void should_return_some_stock_if_there_is_no_reservation_but_stock(){
        Product product=new Product();
        product.setId(new Long(1));
        product.setName("Test");
        product.setPrice(BigDecimal.valueOf(10));

        StockTransactionType purchase_tansaction=new StockTransactionType();
        purchase_tansaction.setId(new Long(1));
        purchase_tansaction.setPositive(new Boolean(true));
        purchase_tansaction.setName("Purchase");

        StockTransaction stockTransaction=new StockTransaction();
        stockTransaction.setId(new Long(1));
        stockTransaction.setQuantity(BigDecimal.valueOf(10));
        stockTransaction.setProduct(product);
        stockTransaction.setStockTransactionType(purchase_tansaction);

        doReturn(Collections.singletonList(stockTransaction)).when(stockTransactionRepository).findByProduct(any(Product.class));
        doReturn(new ArrayList<StockTransaction>()).when(stockReservationRepository).findByProduct(any(Product.class));


        BigDecimal resp=stockManager.stock(new Long(1));
        assertEquals(resp,BigDecimal.valueOf(10));

    }

    @Test
    public void should_return_correct_stock_if_there_is_no_reservation_but_multiple_stock(){
        Product product=new Product();
        product.setId(new Long(1));
        product.setName("Test");
        product.setPrice(BigDecimal.valueOf(10));

        StockTransactionType purchase_tansaction=new StockTransactionType();
        purchase_tansaction.setId(new Long(1));
        purchase_tansaction.setPositive(new Boolean(true));
        purchase_tansaction.setName("Purchase");

        StockTransaction stockTransaction=new StockTransaction();
        stockTransaction.setId(new Long(1));
        stockTransaction.setQuantity(BigDecimal.valueOf(10.8));
        stockTransaction.setProduct(product);
        stockTransaction.setStockTransactionType(purchase_tansaction);

        StockTransactionType sold_tansaction=new StockTransactionType();
        sold_tansaction.setId(new Long(2));
        sold_tansaction.setPositive(new Boolean(false));
        sold_tansaction.setName("Sold");

        StockTransaction stockTransactionSold=new StockTransaction();
        stockTransactionSold.setId(new Long(2));
        stockTransactionSold.setQuantity(BigDecimal.valueOf(2.2));
        stockTransactionSold.setProduct(product);
        stockTransactionSold.setStockTransactionType(sold_tansaction);

        ArrayList<StockTransaction> trans=new ArrayList<>();
        trans.add(stockTransaction);
        trans.add(stockTransactionSold);

        doReturn(trans).when(stockTransactionRepository).findByProduct(any(Product.class));
        doReturn(new ArrayList<StockTransaction>()).when(stockReservationRepository).findByProduct(any(Product.class));


        BigDecimal resp=stockManager.stock(new Long(1));
        assertEquals(BigDecimal.valueOf(8.6),resp);

    }
    @Test
    public void should_return_correct_stock_if_there_are_multiple_reservations_and_some_stock(){
        Product product=new Product();
        product.setId(new Long(1));
        product.setName("Test");
        product.setPrice(BigDecimal.valueOf(10));

        StockTransactionType purchase_tansaction=new StockTransactionType();
        purchase_tansaction.setId(new Long(1));
        purchase_tansaction.setPositive(new Boolean(true));
        purchase_tansaction.setName("Purchase");

        StockTransaction stockTransaction=new StockTransaction();
        stockTransaction.setId(new Long(1));
        stockTransaction.setQuantity(BigDecimal.valueOf(10.8));
        stockTransaction.setProduct(product);
        stockTransaction.setStockTransactionType(purchase_tansaction);

        StockReservation reservation1=new StockReservation();
        reservation1.setPositive(new Boolean(true));
        reservation1.setId(new Long(1));
        reservation1.setProduct(product);
        reservation1.setQuantity(BigDecimal.valueOf(2.2));

        StockReservation reservation2=new StockReservation();
        reservation2.setPositive(new Boolean(true));
        reservation2.setId(new Long(2));
        reservation2.setProduct(product);
        reservation2.setQuantity(BigDecimal.valueOf(2.2));

        StockReservation reservation3=new StockReservation();
        reservation3.setPositive(new Boolean(false));
        reservation3.setId(new Long(3));
        reservation3.setProduct(product);
        reservation3.setQuantity(BigDecimal.valueOf(2.1));

        ArrayList<StockReservation> trans=new ArrayList<>();
        trans.add(reservation1);
        trans.add(reservation2);
        trans.add(reservation3);

        doReturn(Collections.singletonList(stockTransaction)).when(stockTransactionRepository).findByProduct(any(Product.class));
        doReturn(trans).when(stockReservationRepository).findByProduct(any(Product.class));


        BigDecimal resp=stockManager.stock(new Long(1));
        assertEquals(BigDecimal.valueOf(8.5),resp);

    }
}
