package com.k0s.onlineshop.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.k0s.onlineshop.entity.Product;
import com.k0s.onlineshop.exceptions.ProductNotFoundException;
import com.k0s.onlineshop.service.ProductService;
import com.k0s.onlineshop.testcontainers.TestContainer;
import org.flywaydb.core.Flyway;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
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
class AdminProductControllerIntegrationTest extends TestContainer {


    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ProductService productService;

    @Autowired
    protected Flyway flyway;

    @AfterEach
    public void restoreDatabase() {
        flyway.clean();
        flyway.migrate();
    }

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Test
    @DisplayName("AdminController GET-/admin/product | Get All products status ok, empty and non empty list")
    void getAllProducts() throws Exception {

        List<Product> productList = productService.findAll();

        assertFalse(productList.isEmpty());

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/product"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", CoreMatchers.is( productList.size())));
    }

    @Test
    @DisplayName("AdminController POST-/admin/product | Save product with valid params return status ok")
    void saveProductWithValidParamsOK() throws Exception {

        Product product = Product.builder()
                .name("Banana")
                .price(111.1)
                .description("description")
                .build();


        mockMvc.perform(MockMvcRequestBuilders.post("/admin/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .json(objectMapper.writeValueAsString(product)));

        List<Product> banana = productService.search("Banana");
        Product savedProduct = banana.stream().findFirst().get();
        assertEquals(product.getName(), savedProduct.getName());
        assertEquals(product.getPrice(), savedProduct.getPrice(), 0.001);
        assertEquals(product.getDescription(), savedProduct.getDescription());

        System.out.println("==========" + savedProduct.getId());

        savedProduct = productService.findById(4);
        assertEquals(product.getName(), savedProduct.getName());
        assertEquals(product.getPrice(), savedProduct.getPrice(), 0.001);
        assertEquals(product.getDescription(), savedProduct.getDescription());
    }

    @Test
    @DisplayName("AdminController POST-/admin/product | Save product with invalid params return status bad request")
    void saveProductWithInvalidParamsBadRequest() throws Exception {
        Product product = Product.builder()
                .name("Ba")
                .price(111.1)
                .description("description")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product))

        ).andExpect(MockMvcResultMatchers.status().isBadRequest());

        assertThrows(IllegalArgumentException.class, ()-> productService.saveProduct(product));
    }

    @Test
    @DisplayName("AdminController DELETE-/admin/product{id} | Delete product")
    void deleteProductByID() throws Exception {
        Product product = Product.builder()
                .name("Banana")
                .price(111.1)
                .description("description")
                .build();


        mockMvc.perform(MockMvcRequestBuilders.post("/admin/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .json(objectMapper.writeValueAsString(product)));

        mockMvc.perform(MockMvcRequestBuilders.delete("/admin/product/{id}", 4))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Product deleted"));
    }

    @Test
    @DisplayName("AdminController PUT-/admin/product/{id} | Update product with valid params, status OK")
    void updateProductByIDvalidOk() throws Exception {
        Product product = Product.builder()
                .name("Banana")
                .price(111.1)
                .description("description")
                .build();


        Product savedProduct = productService.findById(1);


        mockMvc.perform(MockMvcRequestBuilders.put("/admin/product/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Product updatedProduct = productService.findById(1);

        assertEquals(savedProduct.getId(), updatedProduct.getId());
        assertNotEquals(savedProduct.getName(), updatedProduct.getName());
        assertNotEquals(savedProduct.getDescription(), updatedProduct.getDescription());

    }

    @Test
    @DisplayName("AdminController PUT-/admin/product/{id} | Update product with invalid params, status BAD REQUEST")
    void updateProductByIDinvalidParamsBadReuqest() throws Exception {

        Product product = Product.builder()
                .id(1)
                .name("Ba")
                .price(111.1)
                .description("description")
                .build();


        mockMvc.perform(MockMvcRequestBuilders.put("/admin/product/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product))

        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
        assertThrows(IllegalArgumentException.class, ()-> productService.update(product,1));

    }

    @Test
    @DisplayName("AdminController PUT-/admin/product/{id} | Update product with invalid ID, status NOT FOUND")
    void updateProductByIDinvalidIDNotFound() throws Exception {

        Product product = Product.builder()
                .id(-1)
                .name("Banana")
                .price(111.1)
                .description("description")
                .build();


        mockMvc.perform(MockMvcRequestBuilders.put("/admin/product/{id}", -1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product))

        ).andExpect(MockMvcResultMatchers.status().isNotFound());

        assertThrows(ProductNotFoundException.class, ()-> productService.update(product, -1));


    }


}