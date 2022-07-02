package com.k0s.onlineshop.repository;

import com.k0s.onlineshop.entity.ProductCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface ProductCartRepository extends JpaRepository<ProductCart, Long> {

    Optional<ProductCart> findByUserId(long id);

    Optional<ProductCart> findByUserUsername(String username);
}

