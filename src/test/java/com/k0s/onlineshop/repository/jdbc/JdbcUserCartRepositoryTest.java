package com.k0s.onlineshop.repository.jdbc;

import com.k0s.onlineshop.entity.Product;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class JdbcUserCartRepositoryTest {
    @Autowired
    protected Flyway flyway;

    @AfterEach
    public void restoreDatabase() {
        flyway.clean();
        flyway.migrate();
    }

    @Autowired
    private JdbcUserCartRepository jdbcUserCartRepository;

    @Autowired
    private JdbcProductRepository jdbcProductRepository;


    private String username = "user";
    private long productId = 1;

    @BeforeEach
    void  initProductRepo(){
        Product product = Product.builder()
                .name("testProduct")
                .price(111.11)
                .description("description")
                .build();
        jdbcProductRepository.save(product);
    }

    @Test
    @DisplayName("Add and get product from cart")
    void addToCartAndGet() {

        List<Product> productCart = jdbcUserCartRepository.getProductCart(username);
        assertTrue(productCart.isEmpty());

        jdbcUserCartRepository.addToCart(username,productId);

        productCart = jdbcUserCartRepository.getProductCart(username);
        assertFalse(productCart.isEmpty());
        assertEquals(1, productCart.size());
    }



    @Test
    @Disabled
    void removeProduct() {
        jdbcUserCartRepository.addToCart(username,productId);

        List<Product> productCart = jdbcUserCartRepository.getProductCart(username);
        assertFalse(productCart.isEmpty());
        assertEquals(1, productCart.size());

        jdbcUserCartRepository.removeProduct(username, productId);

        productCart = jdbcUserCartRepository.getProductCart(username);
        assertTrue(productCart.isEmpty());

    }

    @Test
    @DisplayName("Clear user cart")
    void clearCart() {

        jdbcUserCartRepository.addToCart(username,productId);

        List<Product> productCart = jdbcUserCartRepository.getProductCart(username);
        assertFalse(productCart.isEmpty());
        assertEquals(1, productCart.size());

        jdbcUserCartRepository.clearCart(username);

        productCart = jdbcUserCartRepository.getProductCart(username);
        assertTrue(productCart.isEmpty());

    }
}