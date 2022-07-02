package com.k0s.onlineshop.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.k0s.onlineshop.entity.Product;
import com.k0s.onlineshop.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    private ProductService productService;
    private final ObjectMapper objectMapper = new ObjectMapper();
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
    @DisplayName("Get All products status ok, empty and non empty list")
    void getAllProducts() throws Exception {

        when(productService.findAll())
                .thenReturn(new ArrayList<>())
                .thenReturn(new ArrayList<>(List.of(product)));

        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"));

        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(product))));
    }

    @Test
    void search() throws Exception {
        when(productService.search(any()))
                .thenReturn(new ArrayList<>())
                .thenReturn(new ArrayList<>(List.of(product)));


        mockMvc.perform(MockMvcRequestBuilders.get("/search/{value}", "searchValue"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"));

        mockMvc.perform(MockMvcRequestBuilders.get("/search/{value}", "value"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(product))));
    }
}