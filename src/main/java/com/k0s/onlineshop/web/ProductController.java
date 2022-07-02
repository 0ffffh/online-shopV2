package com.k0s.onlineshop.web;

import com.k0s.onlineshop.entity.Product;
import com.k0s.onlineshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("")
    public List<Product> getAll() {
        return productService.findAll();
    }

    @GetMapping("/search/{value}")
    public List<Product> search(@PathVariable("value") String value) {
        return productService.search(value);
    }

}
