package com.k0s.onlineshop.web;

import com.k0s.onlineshop.entity.Product;
import com.k0s.onlineshop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/user/cart")
public class UserCartController {


    @Autowired
    private CartService cartService;

    @GetMapping
    public List<Product> getUserCart(Principal principal) {
        return cartService.getUserCart(principal.getName());
    }


    @PostMapping("product/{id}")
    public Product addProductToUserCart(@PathVariable("id") Long productId, Principal principal) {
        return cartService.addToCart(principal.getName(), productId);
    }


    @DeleteMapping("product/{id}")
    public String deleteProductFromUserCart(@PathVariable("id") Long productId, Principal principal) {
        return cartService.removeProduct(principal.getName(), productId);
    }

    @DeleteMapping("clear")
    public String clearUserCart(Principal principal) {
        return cartService.clearCart(principal.getName());
    }


}
