package com.k0s.onlineshop.integrationTests;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.k0s.onlineshop.entity.Product;
import com.k0s.onlineshop.entity.ProductCart;
import com.k0s.onlineshop.exceptions.ProductCartNotFoundException;
import com.k0s.onlineshop.service.CartService;
import com.k0s.onlineshop.testcontainers.AbstractContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@DBRider
class UserCartControllerIntegrationTest extends AbstractContainer {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    private CartService cartService;

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
    @DataSet(value = {"users.yml", "roles.yml", "users_roles.yml", "products.yml"})
    @DisplayName("Get clear UserCart ")
    void getClearUserCart() throws Exception {

        mockMvc.perform(get("/user/cart").principal(principal))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Cart empty"));

        assertThrows(ProductCartNotFoundException.class,
                () -> cartService.getProductCartByUsername(principal.getName()));

    }

    @Test
    @DataSet(value = {"users.yml", "roles.yml", "users_roles.yml", "products.yml"})
    @DisplayName("Get UserCart with products ")
    void getUserCartWithProducts() throws Exception {

        assertThrows(ProductCartNotFoundException.class,
                () -> cartService.getProductCartByUsername(principal.getName()));


        cartService.addToCart(principal.getName(), 1L);

        ProductCart productCart = cartService.getProductCartByUsername(principal.getName());
        assertThat(productCart).isNotNull();
        assertEquals(1, productCart.getProducts().size());


        mockMvc.perform(get("/user/cart").principal(principal))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("testProduct1"));

        cartService.clearCart(principal.getName());
    }

    @Test
    @DataSet(value = {"users.yml", "roles.yml", "users_roles.yml", "products.yml"})
    @DisplayName("Add product to usercart")
    void addProductToUserCart() throws Exception {

        assertThrows(ProductCartNotFoundException.class,
                () -> cartService.getProductCartByUsername(principal.getName()));

        mockMvc.perform(post("/user/cart/product/{id}", 1)
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("testProduct1"));


        ProductCart productCart = cartService.getProductCartByUsername(principal.getName());
        assertThat(productCart).isNotNull();
        assertEquals(1, productCart.getProducts().size());

        cartService.clearCart(principal.getName());
    }

    @Test
    @DataSet(value = {"users.yml", "roles.yml", "users_roles.yml", "products.yml"})
    @DisplayName("Delete product throws from usercart")
    void deleteProductFromUserCart() throws Exception {

        cartService.addToCart(principal.getName(), 1L);
        mockMvc.perform(delete("/user/cart/product/{id}", 1)
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());


        ProductCart afterCart = cartService.getProductCartByUsername(principal.getName());

        assertThat(afterCart).isNotNull();
        assertThat(afterCart.getProducts().size()).isEqualTo(0);

    }

    @Test
    @DataSet(value = {"users.yml", "roles.yml", "users_roles.yml", "products.yml"})
    @DisplayName("Delete product throws ProductNotFoundExexption()")
    void deleteProductFromUserCartNotFoundThrowProductNotFoundEx() throws Exception {

        mockMvc.perform(delete("/user/cart/product/{id}", 1)
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    @DataSet(value = {"users.yml", "roles.yml", "users_roles.yml", "products.yml"})
    @DisplayName("Clear user cart")
    void clearUserCart() throws Exception {
        cartService.addToCart(principal.getName(), 1L);


        ProductCart productCart = cartService.getProductCartByUsername(principal.getName());
        assertThat(productCart).isNotNull();
        assertEquals(1, productCart.getProducts().size());

        mockMvc.perform(delete("/user/cart", 1)
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());


        assertThrows(ProductCartNotFoundException.class,
                () -> cartService.getProductCartByUsername(principal.getName()));

    }
}