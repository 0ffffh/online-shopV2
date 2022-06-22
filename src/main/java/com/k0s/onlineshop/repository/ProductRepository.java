package com.k0s.onlineshop.repository;

import com.k0s.onlineshop.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    List<Product> findAll();

    Optional<Product> findById(long id);

    Product save(Product product);

    void deleteById(long id);

    Product update(Product product);

    List<Product> search(String value);

}

