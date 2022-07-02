package com.k0s.onlineshop.web;

import com.k0s.onlineshop.entity.Product;
import com.k0s.onlineshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin/product")
public class AdminProductController {

    @Autowired
    private ProductService productService;


    @GetMapping
    public List<Product> getAllProducts() {

        return productService.findAll();
    }

    @GetMapping("{id}")
    public Product getProductByIs(@PathVariable("id") Long id) {

        return productService.findById(id);
    }

    @PostMapping
    public Product saveProduct(@RequestBody Product product) {
        productService.saveProduct(product);
        log.info("Add product");
        log.debug("Add product, {} {} {}", product.getName(), product.getPrice(), product.getDescription());
        return product;
    }


    @DeleteMapping("{id}")
    public String deleteProductByIDn(@PathVariable Long id) {
        productService.remove(id);
        return "Product deleted";
    }


    @PutMapping("{id}")
    public Product updateProductByIDn(@PathVariable("id") Long productId, @RequestBody Product product) {
        return productService.update(product, productId);
    }

}
