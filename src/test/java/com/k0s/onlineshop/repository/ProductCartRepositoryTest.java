package com.k0s.onlineshop.repository;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.k0s.onlineshop.entity.Product;
import com.k0s.onlineshop.entity.ProductCart;
import com.k0s.onlineshop.security.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@DBRider
@DataSet
class ProductCartRepositoryTest {

    @Autowired
    private ProductCartRepository productCartRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    @DataSet(value = {"users.yml", "roles.yml", "users_roles.yml", "products.yml"})
    void deleteProductByIdFromProductCart() {
        String username = "user";
        long productId = 1;
        Product product = productRepository.findById(productId).get();
        User user = userRepository.findByUsername(username).get();


        ProductCart productCart = new ProductCart();
        productCart.setUser(user);

        productCart.addProduct(product);
        ProductCart savedProductCart = productCartRepository.save(productCart);

        assertThat(savedProductCart).isNotNull();
        List<Product> productList = savedProductCart.getProducts();
        assertThat(productList.size()).isEqualTo(1);


        productCartRepository.deleteProductByIdFromProductCart(savedProductCart.getId(), productId);

        Optional<ProductCart> optionalProductCart = productCartRepository.findById(savedProductCart.getId());
        assertThat(optionalProductCart).isPresent();
        productList = optionalProductCart.get().getProducts();
        assertThat(productList.size()).isEqualTo(0);
    }
}