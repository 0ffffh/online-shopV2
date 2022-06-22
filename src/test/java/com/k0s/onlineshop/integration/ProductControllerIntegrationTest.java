package com.k0s.onlineshop.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.k0s.onlineshop.entity.Product;
import com.k0s.onlineshop.service.ProductService;
import com.k0s.onlineshop.testcontainers.TestContainer;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@DirtiesContext
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class ProductControllerIntegrationTest extends TestContainer {



    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ProductService productService;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Test
    @DisplayName("ProductController GET-/  | Get All products status ok, non empty list")
    void getAllProducts() throws Exception {

        List<Product> productList = productService.findAll();

        assertFalse(productList.isEmpty());


        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", CoreMatchers.is( productList.size())));
    }

    @Test
    @DisplayName("ProductController GET-/search | Search by product name find result")
    void searchByProductNameExpectNonEmptyResult() throws Exception {
        List<Product> productList = productService.search("Product");

        assertFalse(productList.isEmpty());
        assertEquals(3, productList.size());


        mockMvc.perform(MockMvcRequestBuilders.get("/search/{value}", "Product"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", CoreMatchers.is( productList.size())));
    }

    @Test
    @DisplayName("ProductController GET-/search  |  Search by product description find result")
    void searchByProductDescriptionExpectNonEmptyResult() throws Exception {
        List<Product> productList = productService.search("Description1");

        assertFalse(productList.isEmpty());
        assertEquals(1, productList.size());

        mockMvc.perform(MockMvcRequestBuilders.get("/search/{value}", "Description1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", CoreMatchers.is( productList.size())));

    }

    @Test
    @DisplayName("ProductController GET-/search |  Search by product name and description find result")
    void searchByProductNameOrDescriptionExpectNonEmptyResult() throws Exception {
        List<Product> productList = productService.search("test");

        assertFalse(productList.isEmpty());
        assertEquals(3, productList.size());

        mockMvc.perform(MockMvcRequestBuilders.get("/search/{value}", "test"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", CoreMatchers.is( productList.size())));
    }

    @Test
    @DisplayName("ProductController GET-/search | Search by product result not found")
    void searchByProductExpectEmptyResult() throws Exception {
        List<Product> productList = productService.search("NotExistProduct");

        assertTrue(productList.isEmpty());

        mockMvc.perform(MockMvcRequestBuilders.get("/search/{value}", "NotExistProduct"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", CoreMatchers.is( productList.size())))
                .andExpect(content().json("[]"));
    }

}