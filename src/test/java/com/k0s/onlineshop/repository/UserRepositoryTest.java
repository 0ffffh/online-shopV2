package com.k0s.onlineshop.repository;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.k0s.onlineshop.security.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DBRider
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Test
    @DataSet(value = {"users.yml", "roles.yml", "users_roles.yml"})
    void findByUsername() {
        Optional<User> user = userRepository.findByUsername("user");
        assertTrue(user.isPresent());
        assertEquals(user.get().getRoles().stream().findFirst().get().getName(), "ROLE_USER");

        user = userRepository.findByUsername("notExistUser");
        assertTrue(user.isEmpty());
    }

    @Test
    @DataSet(value = {"users.yml"}, executeStatementsBefore = "ALTER SEQUENCE user_sequence RESTART WITH 4")
    void getAll() {
        List<User> userList = userRepository.findAll();

        assertFalse(userList.isEmpty());
        assertEquals(3, userList.size());
    }

}