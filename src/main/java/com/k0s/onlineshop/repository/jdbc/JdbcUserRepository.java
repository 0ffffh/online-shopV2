package com.k0s.onlineshop.repository.jdbc;


import com.k0s.onlineshop.repository.UserRepository;
import com.k0s.onlineshop.repository.jdbc.mapper.RoleRowMapper;
import com.k0s.onlineshop.repository.jdbc.mapper.UserRowMapper;
import com.k0s.onlineshop.security.user.Role;
import com.k0s.onlineshop.security.user.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class JdbcUserRepository implements UserRepository {
    private static final String GET_ALL_USERS_QUERY = "SELECT id, username, password FROM users;";
    private static final String GET_USER_BY_NAME_QUERY = "SELECT id, username, password FROM users WHERE username = ?;";
    private static final String GET_USER_ROLES_QUERY = "select users.id, roles.name from users join users_roles on users.id = users_roles.user_id join roles on users_roles.role_id = roles.id where users.username = ?;";

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    private UserRowMapper userRowMapper;

    @Autowired
    private RoleRowMapper roleRowMapper;


    @Override
    @SneakyThrows
    public Optional<AuthUser> findByUsername(String username) {
        List<AuthUser> authUserList = jdbcTemplate.query(GET_USER_BY_NAME_QUERY, userRowMapper, username);

        if (authUserList.isEmpty()) {
            return Optional.empty();
        }
        return authUserList.stream().findFirst();

    }

    @Override
    public List<AuthUser> getAll() {
        return jdbcTemplate.query(GET_ALL_USERS_QUERY, userRowMapper);
    }

    public List<Role> getUserRolesByUsername(String username) {
        return jdbcTemplate.query(GET_USER_ROLES_QUERY, roleRowMapper, username);
    }

}
