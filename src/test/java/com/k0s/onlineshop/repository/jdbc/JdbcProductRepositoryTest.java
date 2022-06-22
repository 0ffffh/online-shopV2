package com.k0s.onlineshop.repository.jdbc;

import com.k0s.onlineshop.entity.Product;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
class JdbcProductRepositoryTest {

    @Autowired
    JdbcProductRepository jdbcProductRepository;

    @Autowired
    protected Flyway flyway;

    @AfterEach
    public void restoreDatabase() {
        flyway.clean();
        flyway.migrate();
    }

    private Product product;

    @BeforeEach
    void setUp() {
         product = Product.builder()
                .name("testProduct1")
                .price(111.11)
                .description("testDescription1")
                .build();
    }



    @Test
    @DisplayName("Find all non empty table")
    void findAll() {

        List<Product> productList = jdbcProductRepository.findAll();
        assertEquals(3, productList.size());

        Product expected = productList.stream().findFirst().get();

        assertEquals(product.getName(), expected.getName());
        assertEquals(product.getPrice(), expected.getPrice(), 0.001);
        assertEquals(product.getDescription(), expected.getDescription());
    }

    @Test
    @DisplayName("Find by id")
    void findById() {

        List<Product> productList = jdbcProductRepository.findAll();

        Product foundProduct = jdbcProductRepository.findById(1).get();
        assertEquals(foundProduct.getName(), product.getName());

        Product product2 = Product.builder()
                .name("testProduct2")
                .price(222.22)
                .description("testDescription2")
                .build();

        jdbcProductRepository.save(product2);
         foundProduct = jdbcProductRepository.findById(2L).get();
        assertEquals(foundProduct.getName(), product2.getName());


        Optional<Product> optionalProduct = jdbcProductRepository.findById(100000L);
        assertTrue(optionalProduct.isEmpty());

    }

    @Test
    @DisplayName("Delete product by id")
    void deleteByIdTest() {

        Optional<Product> addedProduct = jdbcProductRepository.findById(1);

        assertEquals(addedProduct.get().getName(), product.getName());
        assertEquals(addedProduct.get().getPrice(), product.getPrice(), 0.001);
        assertEquals(addedProduct.get().getDescription(), product.getDescription());

        jdbcProductRepository.deleteById(1);

        addedProduct = jdbcProductRepository.findById(1);
        assertTrue(addedProduct.isEmpty());

    }

    @Test
    void update() {

        Product addedProduct = jdbcProductRepository.findById(1).get();

        assertEquals(addedProduct.getName(), product.getName());
        assertEquals(addedProduct.getPrice(), product.getPrice(), 0.001);
        assertEquals(addedProduct.getDescription(), product.getDescription());

        addedProduct.setName("updatedName");
        addedProduct.setPrice(999);

        jdbcProductRepository.update(addedProduct);

        Product updatedProduct = jdbcProductRepository.findById(1).get();

        assertEquals(addedProduct.getId(), updatedProduct.getId());
        assertEquals(addedProduct.getPrice(), updatedProduct.getPrice(), 0.001);

        assertNotEquals(updatedProduct.getName(), product.getName());
        assertNotEquals(updatedProduct.getPrice(), product.getPrice(), 0.001);


    }

    @Test
    @DisplayName("Search test")
    void search() {

        List<Product> searchList = jdbcProductRepository.search("test");
        assertFalse(searchList.isEmpty());

        Product foundProduct = searchList.stream().findFirst().get();

        assertEquals(foundProduct.getName(), product.getName());


        searchList = jdbcProductRepository.search("test");

        assertEquals(3, searchList.size());

    }
}