package product.controllers;

import org.hamcrest.Matchers;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import product.TestUtil;
import product.dto.OrderDTO;
import product.dto.ProductDTO;
import product.model.Product;
import product.services.ProductManager;
import product.services.StockManager;
import product.services.TokenAuthentication;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {ProductDetailsController.class},includeFilters = @ComponentScan.Filter(classes = EnableWebSecurity.class))

public class ProductDetailsControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProductManager productManager;

    @MockBean
    private StockManager stockManager;

    @MockBean
    private TokenAuthentication tokenAuthentication;

    private Authentication authentication;

    @Before
    public void setUp(){
        authentication= new UsernamePasswordAuthenticationToken("authorized", null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))) ;
    }

    @Test
    public void should_return_all_products() throws Exception {
        Product first=new Product();
        first.setId(new Long(1));
        first.setPrice(new BigDecimal("100.0"));
        first.setName("Nescafe");
        Product second=new Product();
        second.setId(new Long(2));
        second.setPrice(new BigDecimal("110.0"));
        second.setName("Tea");
        doReturn(Arrays.asList(first,second)).when(productManager).getAll();
        doReturn(authentication).when(tokenAuthentication).getAuthentication(any(HttpServletRequest.class));
        mvc.perform(get("/product/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id",is(first.getId().intValue())))
                .andExpect(jsonPath("$[0].price",is(first.getPrice().doubleValue())))
                .andExpect(jsonPath("$[0].name",is(first.getName())))
                .andExpect(jsonPath("$[1].id",is(second.getId().intValue())))
                .andExpect(jsonPath("$[1].price",is(second.getPrice().doubleValue())))
                .andExpect(jsonPath("$[1].name",is(second.getName())))


        ;

        verify(productManager,times(1)).getAll();
        verifyNoMoreInteractions(productManager);

    }
    @Test
    public void should_buy_product() throws Exception {
        OrderDTO orderDTO=new OrderDTO();
        orderDTO.setQuantity(BigDecimal.valueOf(100));
        doReturn(authentication).when(tokenAuthentication).getAuthentication(any(HttpServletRequest.class));
        mvc.perform(post("/product/1/buy")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orderDTO)))
                .andExpect(status().isOk());

        verify(stockManager,times(1)).book(new Long(1),BigDecimal.valueOf(100));
        verifyNoMoreInteractions(stockManager);

    }
    @Test
    public void should_create_product() throws Exception {
        Product product=new Product();
        product.setName("Test Product");
        product.setPrice(BigDecimal.valueOf(100.10));
        ProductDTO productDTO=new ProductDTO(product);
        authentication= new UsernamePasswordAuthenticationToken("authorized", null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))) ;
        doReturn(authentication).when(tokenAuthentication).getAuthentication(any(HttpServletRequest.class));
        Product response=new Product();
        response.setName(product.getName());
        response.setPrice(product.getPrice());
        response.setId(new Long(1));
        doReturn(response).when(productManager).create(any(Product.class));
        mvc.perform(post("/product/create")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(productDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(response.getId().intValue())))
                .andExpect(jsonPath("$.name", is(response.getName())))
                .andExpect(jsonPath("$.price", is(response.getPrice().doubleValue())));

        verify(productManager,times(1)).create(any(Product.class));
        verifyNoMoreInteractions(productManager);

    }
    @Test
    public void should_sell_product() throws Exception {
        OrderDTO orderDTO=new OrderDTO();
        orderDTO.setQuantity(BigDecimal.valueOf(100));
        doReturn(authentication).when(tokenAuthentication).getAuthentication(any(HttpServletRequest.class));
        mvc.perform(post("/product/1/sold")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orderDTO)))
                .andExpect(status().isOk());

        verify(stockManager,times(1)).sold(new Long(1),BigDecimal.valueOf(100));
        verifyNoMoreInteractions(stockManager);

    }
    @Test
    public void should_return_stock_for_product() throws Exception {
        doReturn(authentication).when(tokenAuthentication).getAuthentication(any(HttpServletRequest.class));
        doReturn(BigDecimal.valueOf(20)).when(stockManager).stock(any(Long.class));
        MvcResult result = mvc.perform(get("/product/1/stock"))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(result.getResponse().getContentAsString(),"20");
        verify(stockManager,times(1)).stock(new Long(1));
        verifyNoMoreInteractions(stockManager);

    }
}
