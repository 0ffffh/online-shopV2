package com.k0s.onlineshop.repository;

import com.k0s.onlineshop.entity.Product;

import java.util.List;

public interface UserCartRepository {
    void addToCart(String name, Long productId);

    List<Product> getProductCart(String name);

    int removeProduct(String name, Long productId);

    int clearCart(String username);

}
