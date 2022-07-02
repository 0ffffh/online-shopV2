package com.k0s.onlineshop.service;

import com.k0s.onlineshop.entity.Product;
import com.k0s.onlineshop.exceptions.ProductNotFoundException;
import com.k0s.onlineshop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    @Autowired
    private final ProductRepository productRepository;


    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findById(long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isEmpty()) {
            throw new ProductNotFoundException("Product id = " + id + " not found");
        }
        return optionalProduct.get();
    }


    public Product saveProduct(Product product) {
        validate(product);
        return productRepository.save(product);
    }


    public void remove(long id) {
        productRepository.deleteById(id);
    }

    public Product update(Product product, long productId) {
        validate(product);
        product.setId(productId);
        product.setCreationDate(LocalDateTime.now());

        if (productRepository.findById(product.getId()).isPresent()) {
            return productRepository.save(product);
        }
        throw new ProductNotFoundException("Update error product id " + product.getId() + " not found");
    }

    public List<Product> search(String value) {
        return productRepository.findByProductNameOrDescription(value);
    }


    protected void validate(Product product) {
        if (product.getName() == null || product.getName().length() < 3 || product.getName().isBlank()) {
            throw new IllegalArgumentException("Product name length must be > 3");
        }

        if (product.getPrice() < 0) {
            throw new IllegalArgumentException("Product price  must be > 0");
        }
    }

}
