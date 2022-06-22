package com.k0s.onlineshop.service;

import com.k0s.onlineshop.entity.Product;
import com.k0s.onlineshop.exceptions.ProductNotFoundException;
import com.k0s.onlineshop.repository.UserCartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class CartServiceTest {

    @Autowired
    CartService cartService;

    @MockBean
    UserCartRepository userCartRepository;

    @MockBean
    ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1);
        product.setName("productName");
    }

    @Test
    @DisplayName("Add product to cart")
    void addToCart() {
        when(productService.findById(anyLong())).thenReturn(product);

        Product product = cartService.addToCart("username", 1L);

        assertEquals(product.getName(), this.product.getName());
        assertEquals(product.getId(), this.product.getId());

        verify(productService, times(1)).findById(anyLong());
        verify(userCartRepository, times(1)).addToCart(anyString(), anyLong());

    }

    @Test
    @DisplayName("Add product to cart")
    void addToCartProductNotFound() {
        when(productService.findById(anyLong())).thenThrow(ProductNotFoundException.class);

        assertThrows(ProductNotFoundException.class, ()-> cartService.addToCart(anyString(), 1L));

        verify(productService, times(1)).findById(anyLong());
        verify(userCartRepository, never()).addToCart(anyString(), anyLong());

    }

    @Test
    @DisplayName("Get empty usercard")
    void getEmptyUserCart() {
        when(userCartRepository.getProductCart(anyString())).thenReturn(List.of());

        List<Product> products =cartService.getUserCart(anyString());
        assertEquals(products.size(), 0);
        verify(userCartRepository, times(1)).getProductCart(anyString());
    }

    @Test
    @DisplayName("Get not empty usercard")
    void getUserCart() {
        when(userCartRepository.getProductCart(anyString())).thenReturn(List.of(product));

        List<Product> products =cartService.getUserCart(anyString());
        assertEquals(products.size(), 1);
        verify(userCartRepository, times(1)).getProductCart(anyString());
    }

    @Test
    @DisplayName("Remove product from cart. Product not found")
    void removeProductThrowNotFoundEx() {
        when(userCartRepository.removeProduct(anyString(),anyLong())).thenReturn(0);

        assertThrows(ProductNotFoundException.class, ()-> cartService.removeProduct(anyString(), anyLong()));

        verify(userCartRepository, times(1)).removeProduct(anyString(), anyLong());
    }

    @Test
    @DisplayName("Remove product from cart.")
    void removeProduct() {
        when(userCartRepository.removeProduct(anyString(),anyLong())).thenReturn(1);


        assertEquals("Product deleted", cartService.removeProduct(anyString(), anyLong()));
        verify(userCartRepository, times(1)).removeProduct(anyString(), anyLong());
    }

    @Test
    void clearCart() {

        assertEquals("Cart cleared", cartService.clearCart(anyString()));

        verify(userCartRepository, times(1)).clearCart(anyString());
    }
}