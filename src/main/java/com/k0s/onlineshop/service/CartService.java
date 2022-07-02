package com.k0s.onlineshop.service;


import com.k0s.onlineshop.entity.Product;
import com.k0s.onlineshop.entity.ProductCart;
import com.k0s.onlineshop.exceptions.ProductCartNotFoundException;
import com.k0s.onlineshop.repository.ProductCartRepository;
import com.k0s.onlineshop.repository.ProductRepository;
import com.k0s.onlineshop.repository.UserRepository;
import com.k0s.onlineshop.security.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.support.ReflectivePropertyAccessor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    @Autowired
    private ProductCartRepository productCartRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;


    public ProductCart getProductCartByUsername(String username) {
        User user = userService.findByUsername(username);
            if(user.getProductCart() == null){
                return ProductCart.builder()
                        .user(user)
                        .products(new ArrayList<>())
                        .build();
        }
        return user.getProductCart();
    }

    public ProductCart addToCart(String username, Long productId) {
        ProductCart productCart = getProductCartByUsername(username);

        Product product = productService.findById(productId);

        productCart.addProduct(product);

        return productCartRepository.save(productCart);

    }

    public void removeProductFromCartByProductId(String username, Long productId) {
        ProductCart productCart = getProductCartByUsername(username);
        productCartRepository.delete(productCart);
        productCart.getProducts().removeIf(product -> product.getId()==productId);

        productCartRepository.save(productCart);

    }

    public void clearCart(String username) {
        ProductCart productCart = getProductCartByUsername(username);
        productCartRepository.delete(productCart);
    }
}
