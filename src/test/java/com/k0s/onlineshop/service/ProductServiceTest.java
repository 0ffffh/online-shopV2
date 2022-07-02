package com.k0s.onlineshop.service;

import com.k0s.onlineshop.entity.Product;
import com.k0s.onlineshop.exceptions.ProductNotFoundException;
import com.k0s.onlineshop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class ProductServiceTest {
    @Autowired
    private ProductService productService;
    @MockBean
    ProductRepository productRepository;
    private Product product;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .id(1L)
                .name("Banana")
                .price(111.1)
                .description("description")
                .build();
    }

    @Test
    @DisplayName("Find all")
    void findAll() {
        when(productRepository.findAll()).thenReturn(List.of(product));
        List<Product> productList = productService.findAll();
        assertEquals(1, productList.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Find by id")
    void findById() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        String name = "Banana";
        Product foundProduct = productService.findById(1L);
        assertEquals(name, foundProduct.getName());
        verify(productRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Find by id, product not found exception")
    void findByIdThrowProductNotFoundException() {
        when(productRepository.findById(anyLong())).thenThrow(ProductNotFoundException.class);

        assertThrows(ProductNotFoundException.class, () -> productService.findById(1L));
        verify(productRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Save product")
    void saveProduct() {
        when(productRepository.save(any())).thenReturn(product);

        Product saveProduct = productService.saveProduct(product);
        verify(productRepository, times(1)).save(product);
        assertEquals(saveProduct.getName(), product.getName());

    }

    @Test
    @DisplayName("Save product with illegal arguments")
    void saveProductThrowsIllegalArgumentException() {
        product.setName("tt");
        assertThrows(IllegalArgumentException.class, () -> productService.saveProduct(product));
        verify(productRepository, never()).save(product);

    }

    @Test
    void remove() {
        productService.remove(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Update product")
    void update() {
        when(productRepository.save(any())).thenReturn(product);
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        productService.update(product, anyLong());
        verify(productRepository, times(1)).save(product);
        verify(productRepository, times(1)).findById(product.getId());
    }

    @Test
    @DisplayName("Update non existing product, throw ProductNotFoundException")
    void updateNonExistingProductThrowProductNotFoundException() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.update(product, 1));

        verify(productRepository, never()).save(product);
        verify(productRepository, times(1)).findById(product.getId());
    }

    @Test
    @DisplayName("Update product with invalid args, throw IllegalArgumentException")
    void updateProductWithInvalidArgThrowIllegalArgumentException() {
        product.setName("tt");
        assertThrows(IllegalArgumentException.class, () -> productService.update(product, 1));

        verify(productRepository, never()).save(product);
        verify(productRepository, never()).findById(product.getId());
    }

    @Test
    @DisplayName("Search product")
    void search() {
        when(productRepository.findByProductNameOrDescription(any())).thenReturn(List.of(product));
        List<Product> productList = productService.search(anyString());
        assertEquals(1, productList.size());
        verify(productRepository, times(1)).findByProductNameOrDescription(anyString());
    }

    @Test
    @DisplayName("Validate product")
    void validateProductTest() {
        product.setPrice(-1);
        assertThrows(IllegalArgumentException.class, () -> productService.validate(product));

        product.setName("");
        assertThrows(IllegalArgumentException.class, () -> productService.validate(product));

        product.setName("tt");
        assertThrows(IllegalArgumentException.class, () -> productService.validate(product));
    }
}