package com.k0s.onlineshop.repository.jdbc;

import com.k0s.onlineshop.security.user.AuthUser;
import com.k0s.onlineshop.security.user.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class JdbcUserRepositoryTest {

    @Autowired
    JdbcUserRepository jdbcUserRepository;


    @Test
    void findByUsername() {
        Optional<AuthUser> user = jdbcUserRepository.findByUsername("user");
        assertTrue(user.isPresent());

         user = jdbcUserRepository.findByUsername("notExistUser");
        assertTrue(user.isEmpty());
    }

    @Test
    void getAll() {
        List<AuthUser> authUserList = jdbcUserRepository.getAll();

        assertFalse(authUserList.isEmpty());
        assertEquals(3, authUserList.size());
    }

    @Test
    void getUserRolesByUsername() {
        List<Role> user = jdbcUserRepository.getUserRolesByUsername("user");
        assertFalse(user.isEmpty());

        user = jdbcUserRepository.getUserRolesByUsername("notExistUser");
        assertTrue(user.isEmpty());

    }
}