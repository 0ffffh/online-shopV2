package com.k0s.onlineshop.repository.jdbc.mapper;

import com.k0s.onlineshop.entity.Product;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ProductRowMapper implements RowMapper<Product> {
    @Override
    public Product mapRow(ResultSet rs, int rowNum) throws SQLException {

        return new Product(rs.getLong("id"),
                rs.getString("name"),
                rs.getDouble("price"),
                rs.getTimestamp("creation_date").toLocalDateTime(),
                rs.getString("description") );

    }
}
