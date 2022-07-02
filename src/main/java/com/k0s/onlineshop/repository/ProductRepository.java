package com.k0s.onlineshop.repository;

import com.k0s.onlineshop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "SELECT product FROM Product product " +
            "WHERE UPPER(product.name) LIKE UPPER(concat('%', ?1,'%')) " +
            "OR UPPER(product.description) LIKE UPPER(concat('%', ?1,'%'))" +
            " order by product.id")
    List<Product> findByProductNameOrDescription(String value);


}

