package com.k0s.onlineshop.service;


import com.k0s.onlineshop.entity.Product;
import com.k0s.onlineshop.exceptions.ProductNotFoundException;
import com.k0s.onlineshop.repository.UserCartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {
    @Autowired
    private final UserCartRepository userCartRepository;

    @Autowired
    private ProductService productService;

    public Product addToCart(String name, Long productId) {
        Product product = productService.findById(productId);
        userCartRepository.addToCart(name, product.getId());
        return product;
    }

    public List<Product> getUserCart(String name) {
        return userCartRepository.getProductCart(name);
    }

    public String removeProduct(String name, Long productId) {
        if(userCartRepository.removeProduct(name, productId) == 0){
            throw new ProductNotFoundException(String.format("Product id = %d in %s cart not found", productId, name));
        }
        return "Product deleted";
    }

    public String clearCart(String username) {
        userCartRepository.clearCart(username);
        return "Cart cleared";
    }
}
