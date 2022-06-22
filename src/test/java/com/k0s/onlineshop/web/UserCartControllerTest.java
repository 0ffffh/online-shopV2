package com.k0s.onlineshop.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.k0s.onlineshop.entity.Product;
import com.k0s.onlineshop.exceptions.ProductNotFoundException;
import com.k0s.onlineshop.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserCartController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserCartControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CartService cartService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    Principal principal;

    private Product product;


    @BeforeEach
    void setUp() {
        when(principal.getName()).thenReturn("user");

        product = Product.builder()
                .id(1L)
                .name("Banana")
                .price(111.1)
                .description("description")
                .build();
    }

    @Test
    @DisplayName("Get clear UserCart ")
    void getClearUserCart() throws Exception {

        mockMvc.perform(get("/user/cart").principal(principal))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"));
    }

    @Test
    @DisplayName("Get UserCart with products ")
    void getUserCartWithProducts() throws Exception {

        when(cartService.getUserCart(anyString())).thenReturn(List.of(product));
        mockMvc.perform(get("/user/cart").principal(principal))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(product))));

    }

    @Test
    @DisplayName("Add product to usercart")
    void addProductToUserCart() throws Exception {
        when(cartService.addToCart(eq(principal.getName()), anyLong())).thenReturn(product);


        mockMvc.perform(post("/user/cart/product/{id}", 1)
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .json(objectMapper.writeValueAsString(product)));
    }

    @Test
    void deleteProductFromUserCart() throws Exception {
        when(cartService.removeProduct(eq(principal.getName()), anyLong()))
                .thenReturn("ok");

        mockMvc.perform(delete("/user/cart/product/{id}", 1)
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("ok"));

        verify(cartService, times(1)).removeProduct(anyString(), anyLong());
    }

    @Test
    void deleteProductFromUserCartNotFoundThrowProductNotFoundEx() throws Exception {
        when(cartService.removeProduct(eq(principal.getName()), anyLong()))
                .thenThrow(new ProductNotFoundException(""));

        mockMvc.perform(delete("/user/cart/product/{id}", 1)
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(cartService, times(1)).removeProduct(anyString(), anyLong());
    }

    @Test
    void clearUserCart() throws Exception {
        when(cartService.clearCart(eq(principal.getName()))).thenReturn("ok");

        mockMvc.perform(delete("/user/cart/clear", 1)
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("ok"));

        verify(cartService, times(1)).clearCart(anyString());
    }
}