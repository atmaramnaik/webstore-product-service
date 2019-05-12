package product.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import product.dto.OrderDTO;
import product.dto.ProductDTO;
import product.model.Product;
import product.model.StockTransaction;
import product.services.ProductManager;
import product.services.StockManager;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping(path = "/product")
public class ProductDetailsController {

    @Autowired
    private ProductManager productManager;
    @Autowired
    private StockManager stockManager;

    @GetMapping(path="/all")
    public List<ProductDTO> all(){
        return productManager.getAll().stream().map(ProductDTO::new).collect(Collectors.toList());
    }

    @PostMapping(path = "/{id}/buy")
    public boolean buy(@PathVariable(name = "id") Long id,@RequestBody OrderDTO order){
        stockManager.book(id,order.getQuantity());
        return true;
    }

    @PostMapping(path = "/create")
    public ProductDTO buy(@RequestBody ProductDTO productDTO){
        Product product=new Product();
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        return new ProductDTO(productManager.create(product));
    }

    @PostMapping(path = "/{id}/sold")
    public boolean sold(@PathVariable(name = "id") Long id,@RequestBody OrderDTO order){
        stockManager.sold(id,order.getQuantity());
        return true;
    }

    @PostMapping(path = "/{id}/purchase")
    public boolean purchase(@PathVariable(name = "id") Long id,@RequestBody OrderDTO order){
        stockManager.purchase(id,order.getQuantity());
        return true;
    }

    @GetMapping(path="/{id}/stock")
    public BigDecimal stock(@PathVariable(name = "id") Long id){
        return stockManager.stock(id);
    }

//    @GetMapping(path="/test")
//    public boolean test(){
//        return true;
//    }
}
