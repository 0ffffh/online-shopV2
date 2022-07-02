package com.k0s.onlineshop.repository;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.k0s.onlineshop.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
@DBRider
class ProductRepositoryTest {
    @Autowired
    ProductRepository productRepository;
    private Product product;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .name("testProduct1")
                .price(111.11)
                .description("testDescription1")
                .creationDate(LocalDateTime.of(
                        1111, 11, 11,
                        11, 11, 11))
                .build();
    }

    @Test
    @DataSet("products.yml")
    @DisplayName("Find all non empty table")
    void findAll() {

        List<Product> productList = productRepository.findAll();
        assertEquals(3, productList.size());

        Product expected = productList.stream().findFirst().get();

        assertEquals(product.getName(), expected.getName());
        assertEquals(product.getPrice(), expected.getPrice(), 0.001);
        assertEquals(product.getDescription(), expected.getDescription());
        assertEquals(product.getCreationDate(), expected.getCreationDate());
    }

    @Test
    @DataSet("products.yml")
    @DisplayName("Find by id")
    void findById() {

        List<Product> productList = productRepository.findAll();

        Product foundProduct = productRepository.findById(1L).get();
        assertEquals(product.getName(), foundProduct.getName());
        assertEquals(product.getPrice(), foundProduct.getPrice(), 0.001);
        assertEquals(product.getDescription(), foundProduct.getDescription());
        assertEquals(product.getCreationDate(), foundProduct.getCreationDate());

        Product product2 = Product.builder()
                .name("testProduct2")
                .price(222.22)
                .description("testDescription2")
                .creationDate(LocalDateTime.of(
                        2222, 02, 02,
                        02, 02, 02))
                .build();

        foundProduct = productRepository.findById(2L).get();


        assertEquals(product2.getName(), foundProduct.getName());
        assertEquals(product2.getPrice(), foundProduct.getPrice(), 0.001);
        assertEquals(product2.getDescription(), foundProduct.getDescription());
        assertEquals(product2.getCreationDate(), foundProduct.getCreationDate());


        Optional<Product> optionalProduct = productRepository.findById(100000L);
        assertTrue(optionalProduct.isEmpty());

    }

    @Test
    @DataSet("products.yml")
    @DisplayName("Delete product by id")
    void deleteByIdTest() {

        Optional<Product> addedProduct = productRepository.findById(1L);

        assertEquals(addedProduct.get().getName(), product.getName());
        assertEquals(addedProduct.get().getPrice(), product.getPrice(), 0.001);
        assertEquals(addedProduct.get().getDescription(), product.getDescription());
        assertEquals(addedProduct.get().getCreationDate(), product.getCreationDate());

        productRepository.deleteById(1L);

        addedProduct = productRepository.findById(1L);
        assertTrue(addedProduct.isEmpty());

    }

    @Test
    @DataSet("products.yml")
    @DisplayName("Update product")
    void update() {

        Product addedProduct = productRepository.findById(1L).get();

        assertEquals(addedProduct.getName(), product.getName());
        assertEquals(addedProduct.getPrice(), product.getPrice(), 0.001);
        assertEquals(addedProduct.getDescription(), product.getDescription());
        assertEquals(addedProduct.getCreationDate(), product.getCreationDate());

        addedProduct.setName("updatedName");
        addedProduct.setPrice(999);

        productRepository.save(addedProduct);

        Product updatedProduct = productRepository.findById(1L).get();

        assertEquals(addedProduct.getId(), updatedProduct.getId());
        assertEquals(addedProduct.getPrice(), updatedProduct.getPrice(), 0.001);

        assertNotEquals(updatedProduct.getName(), product.getName());
        assertNotEquals(updatedProduct.getPrice(), product.getPrice(), 0.001);

    }

    @Test
    @DataSet("products.yml")
    @DisplayName("Search test")
    void search() {

        List<Product> searchList = productRepository.findByProductNameOrDescription("test");
        assertFalse(searchList.isEmpty());

        Product foundProduct = searchList.stream().findFirst().get();
        assertEquals(foundProduct.getName(), product.getName());

        searchList = productRepository.findByProductNameOrDescription("test");
        assertEquals(3, searchList.size());

    }
}