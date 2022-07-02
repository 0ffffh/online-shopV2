package com.k0s.onlineshop.repository;

import com.k0s.onlineshop.entity.ProductCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface ProductCartRepository extends JpaRepository<ProductCart, Long> {


    @Modifying
    @Transactional
    @Query(value = "DELETE FROM cart_item ci WHERE ci.cart_id = :cartId AND ci.product_id = :productId",
            nativeQuery = true)
    void deleteProductByIdFromProductCart(
            @Param("cartId") Long cartId,
            @Param("productId") Long productId);

}
