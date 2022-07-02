package com.k0s.onlineshop.service;

import com.k0s.onlineshop.entity.Product;
import com.k0s.onlineshop.entity.ProductCart;
import com.k0s.onlineshop.repository.ProductCartRepository;
import com.k0s.onlineshop.security.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.security.Principal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@ActiveProfiles("test")
class CartServiceTest {

    @Autowired
    private CartService cartService;

    @MockBean
    private ProductCartRepository productCartRepository;

    @MockBean
    private UserService userService;
    
    @MockBean
    private ProductService productService;


    @Test
    @DisplayName("Get product cart if exist")
    void getProductCartByUsernameExist() {
        when(productCartRepository.findByUserUsername(anyString()))
                .thenReturn(Optional.of(new ProductCart()));

        ProductCart productCart = cartService.getProductCartByUsername("user");

        assertThat(productCart).isNotNull();
        verify(productCartRepository, times(1)).findByUserUsername(anyString());
        verify(userService, times(0)).findByUsername(eq("user"));
    }

    @Test
    @DisplayName("Get product cart if not exist")
    void getProductCartByUsernameNotExist() {
        when(productCartRepository.findByUserUsername(anyString()))
                .thenReturn(Optional.empty());
        when(userService.findByUsername(anyString())).thenReturn(new User());

        ProductCart productCart = cartService.getProductCartByUsername("user");

        assertThat(productCart).isNotNull();
        verify(productCartRepository, times(1)).findByUserUsername(anyString());
        verify(userService, times(1)).findByUsername(anyString());
    }

    @Test
    void addToCart() {
        ProductCart productCart = new ProductCart();
        when(productCartRepository.findByUserUsername(anyString()))
                .thenReturn(Optional.of(productCart));
        when(productService.findById(anyLong())).thenReturn(new Product());
        when(productCartRepository.save(productCart))
                .thenReturn(productCart);


        ProductCart expected = cartService.addToCart("username", 1L);
        assertThat(expected).isNotNull();

        verify(productCartRepository, times(1)).findByUserUsername(anyString());
        verify(productService, times(1)).findById(eq(1L));
        verify(productCartRepository, times(1)).save(productCart);

    }

    @Test
    void removeProductFromCartByProductId() {
        ProductCart productCart = new ProductCart();
        when(productCartRepository.save(productCart))
                .thenReturn(productCart);
        when(userService.findByUsername(anyString())).thenReturn(new User());
        when(productCartRepository.findByUserUsername(anyString()))
                .thenReturn(Optional.of(productCart));

        ProductCart removed =  cartService.removeProductFromCartByProductId("username",1L);

       assertThat(removed).isNotNull();

        verify(productCartRepository, times(1)).save(productCart);
        verify(productCartRepository, times(1)).delete(productCart);

    }

    @Test
    void clearCart() {
        when(productCartRepository.findByUserUsername(anyString()))
                .thenReturn(Optional.of(new ProductCart()));

        cartService.clearCart("username");

        verify(productCartRepository, times(1)).findByUserUsername(eq("username"));
        verify(productCartRepository, times(1)).delete(any());

    }
}