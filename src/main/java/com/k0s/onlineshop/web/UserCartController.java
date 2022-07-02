package com.k0s.onlineshop.web;

import com.k0s.onlineshop.entity.Product;
import com.k0s.onlineshop.entity.ProductCart;
import com.k0s.onlineshop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/user/cart")
public class UserCartController {


    @Autowired
    private CartService cartService;

    @GetMapping
    public List<Product> getProductCart(Principal principal) {
        ProductCart productCart = cartService.getProductCartByUsername(principal.getName());
        if (productCart == null) {
            return new ArrayList<>(1);
        }
        return productCart.getProducts();
    }


    @PostMapping("product/{id}")
    public List<Product> addProductToProductCart(@PathVariable("id") Long productId, Principal principal) {
        return cartService.addToCart(principal.getName(), productId).getProducts();
    }


    @DeleteMapping("product/{id}")
    public void deleteProductFromProductCart(@PathVariable("id") Long productId, Principal principal) {
        cartService.removeProductFromCartByProductId(principal.getName(), productId);
    }

    @DeleteMapping
    public void clearUserCart(Principal principal) {
        cartService.clearCart(principal.getName());
    }


}
