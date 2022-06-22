package com.k0s.onlineshop.repository.jdbc;


import com.k0s.onlineshop.entity.Product;
import com.k0s.onlineshop.exceptions.SaveProductFailureException;
import com.k0s.onlineshop.repository.ProductRepository;
import com.k0s.onlineshop.repository.jdbc.mapper.ProductRowMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcProductRepository implements ProductRepository {

    private static final String FIND_ALL_QUERY = "SELECT id, name, price, creation_date, description FROM products";
    private static final String FIND_PRODUCT_BY_ID_QUERY = "SELECT id, name, price, creation_date, description FROM products WHERE id = ?";
    private static final String SAVE_PRODUCT_QUERY = "INSERT INTO products (name, price, creation_date, description) values (?, ?, ?, ?);";
    private static final String REMOVE_PRODUCT_QUERY = "DELETE FROM products WHERE id = ?;";
    private static final String UPDATE_PRODUCT_QUERY = "UPDATE products SET name = ?, price = ?, creation_date = ?, description = ?  WHERE id = ?;";
    private static final String SEARCH_PRODUCT_QUERY = "SELECT id, name, price, creation_date, description FROM products WHERE LOWER(name) LIKE LOWER(?) OR LOWER(description) LIKE LOWER(?)";


    @Autowired
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    private ProductRowMapper productRowMapper;


    @Override
    public List<Product> findAll() {
        return jdbcTemplate.query(FIND_ALL_QUERY, productRowMapper);
    }

    @Override
    public Optional<Product> findById(long id) {
        return jdbcTemplate.query(FIND_PRODUCT_BY_ID_QUERY, productRowMapper, id).stream().findFirst();
    }


    @Override
    public Product save(@NonNull Product product) {
        if(jdbcTemplate.update(SAVE_PRODUCT_QUERY, product.getName(), product.getPrice(),
                Timestamp.valueOf(LocalDateTime.now()), product.getDescription()) != 0)
        {
            return product;
        }
        throw new SaveProductFailureException("Save product error");
    }

    @Override
    public void deleteById(long id) {
        jdbcTemplate.update(REMOVE_PRODUCT_QUERY, id);
    }

    @Override
    public Product update(@NonNull Product product) {
        jdbcTemplate.update(UPDATE_PRODUCT_QUERY, product.getName(), product.getPrice(),
                Timestamp.valueOf(product.getCreationDate()),  product.getDescription(), product.getId());
        return findById(product.getId()).get();
    }

    @Override
    public List<Product> search(String value) {
        String search = "%" + value + "%";
        return jdbcTemplate.query(SEARCH_PRODUCT_QUERY, productRowMapper, search, search);
    }

}
