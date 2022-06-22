package com.k0s.onlineshop.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.k0s.onlineshop.entity.Product;
import com.k0s.onlineshop.exceptions.ProductNotFoundException;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@WebMvcTest(AdminProductController.class)
@AutoConfigureMockMvc(addFilters = false)
class AdminProductControllerTest {

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

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/product"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"));

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/product"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(product))));

        assertEquals(productService.findAll().size(), 1);

    }

    @Test
    @DisplayName("Save product with valid params return status ok")
    void saveProductWithValidParamsOK() throws Exception {

        when(productService.saveProduct(product)).thenReturn(product);

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .json(objectMapper.writeValueAsString(product)));

    }

    @Test
    @DisplayName("Save product with invalid params return status bad request")
    void saveProductWithInvalidParamsBadRequest() throws Exception {
        when(productService.saveProduct(any())).thenThrow(IllegalArgumentException.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product))

        ).andExpect(MockMvcResultMatchers.status().isBadRequest());

        verify(productService, times(1)).saveProduct(any());


    }

    @Test
    @DisplayName("Delete product")
    void deleteProductByID() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/admin/product/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Product deleted"));
    }

    @Test
    @DisplayName("Update product with valid params, status OK")
    void updateProductByIDvalidOk() throws Exception {
        when(productService.update(any(), anyLong())).thenReturn(product);

        mockMvc.perform(MockMvcRequestBuilders.put("/admin/product/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(product)));
    }

    @Test
    @DisplayName("Update product with invalid params, status BAD REQUEST")
    void updateProductByIDinvalidParamsBadReuqest() throws Exception {

        when(productService.update(any(), anyLong()))
                .thenThrow(IllegalArgumentException.class);


        mockMvc.perform(MockMvcRequestBuilders.put("/admin/product/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product))

        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Update product with invalid ID, status NOT FOUND")
    void updateProductByIDinvalidIDNotFound() throws Exception {

        when(productService.update(any(), anyLong()))
                .thenThrow(ProductNotFoundException.class);


        mockMvc.perform(MockMvcRequestBuilders.put("/admin/product/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product))

        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }


}