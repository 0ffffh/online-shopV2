package com.k0s.onlineshop.service;


import com.k0s.onlineshop.entity.Product;
import com.k0s.onlineshop.entity.ProductCart;
import com.k0s.onlineshop.exceptions.ProductCartNotFoundException;
import com.k0s.onlineshop.repository.ProductCartRepository;
import com.k0s.onlineshop.security.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {

    @Autowired
    ProductCartRepository productCartRepository;
    @Autowired
    UserService userService;
    @Autowired
    private ProductService productService;


    public ProductCart getProductCartByUsername(String username) {
        User user = (User) userService.loadUserByUsername(username);
        ProductCart productCart = user.getProductCart();
        if (productCart == null) {
            throw new ProductCartNotFoundException("Cart empty");
        }
        return productCart;
    }

    public ProductCart addToCart(String username, Long productId) {
        Product product = productService.findById(productId);
        User user = (User) userService.loadUserByUsername(username);


        ProductCart productCart = user.getProductCart();

        if (productCart == null) {
            productCart = new ProductCart();
            productCart.setUser(user);
        }
        productCart.addProduct(product);
        return productCartRepository.save(productCart);
    }

    public void removeProductFromCartByProductId(String username, Long productId) {
        User user = (User) userService.loadUserByUsername(username);
        ProductCart productCart = user.getProductCart();
        if (productCart == null) {
            throw new ProductCartNotFoundException("Cart empty");
        }
        productCart.deleteProduct(productId);
        user.setProductCart(productCart);
        userService.save(user);
        productCartRepository.deleteProductByIdFromProductCart(productCart.getId(), productId);

    }

    public void clearCart(String username) {
        User user = (User) userService.loadUserByUsername(username);
        productCartRepository.delete(user.getProductCart());
    }
}
