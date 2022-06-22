package com.k0s.onlineshop.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.k0s.onlineshop.entity.Product;
import com.k0s.onlineshop.service.CartService;
import com.k0s.onlineshop.testcontainers.TestContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@DirtiesContext
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class UserCartControllerIntegrationTest extends TestContainer {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private CartService cartService;

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

        List<Product> userCart = cartService.getUserCart(principal.getName());
        assertTrue(userCart.isEmpty());

    }

    @Test
    @DisplayName("Get UserCart with products ")
    void getUserCartWithProducts() throws Exception {
        List<Product> userCart = cartService.getUserCart(principal.getName());
        assertTrue(userCart.isEmpty());

        cartService.addToCart(principal.getName(), 1L);

        userCart = cartService.getUserCart(principal.getName());
        assertEquals(1, userCart.size());


        mockMvc.perform(get("/user/cart").principal(principal))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("testProduct1"));

        cartService.clearCart(principal.getName());
    }

    @Test
    @DisplayName("Add product to usercart")
    void addProductToUserCart() throws Exception {
        List<Product> userCart = cartService.getUserCart(principal.getName());
        assertTrue(userCart.isEmpty());

        mockMvc.perform(post("/user/cart/product/{id}", 1)
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("testProduct1"));


        userCart = cartService.getUserCart(principal.getName());
        assertEquals(1, userCart.size());

        cartService.clearCart(principal.getName());
    }

    @Test
    void deleteProductFromUserCart() throws Exception {


        cartService.addToCart(principal.getName(), 1L);

        mockMvc.perform(delete("/user/cart/product/{id}", 1)
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Product deleted"));

        List<Product> userCart = cartService.getUserCart(principal.getName());
        assertTrue(userCart.isEmpty());
    }

    @Test
    void deleteProductFromUserCartNotFoundThrowProductNotFoundEx() throws Exception {


        mockMvc.perform(delete("/user/cart/product/{id}", 1)
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    void clearUserCart() throws Exception {
        cartService.addToCart(principal.getName(), 1L);

        List<Product> userCart = cartService.getUserCart(principal.getName());
        assertEquals(1, userCart.size());

        mockMvc.perform(delete("/user/cart/clear", 1)
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Cart cleared"));

        userCart = cartService.getUserCart(principal.getName());
        assertTrue(userCart.isEmpty());

    }
}