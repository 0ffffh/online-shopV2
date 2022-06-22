package com.k0s.onlineshop.repository.jdbc.mapper;


import com.k0s.onlineshop.security.user.AuthUser;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserRowMapper implements RowMapper<AuthUser> {

    @Override
    public AuthUser mapRow(ResultSet resultSet, int rowNum) throws SQLException {

        AuthUser authUser = new AuthUser();
        authUser.setId(resultSet.getLong("id"));
        authUser.setUsername(resultSet.getString("username"));
        authUser.setPassword(resultSet.getString("password"));
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);

        return authUser;

    }
}

