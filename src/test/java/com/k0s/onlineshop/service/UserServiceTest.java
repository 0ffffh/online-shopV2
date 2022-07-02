package com.k0s.onlineshop.service;

import com.k0s.onlineshop.repository.UserRepository;
import com.k0s.onlineshop.security.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceTest {
    @Autowired
    private UserService userService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("user");
    }

    @Test
    @DisplayName("Get empty user list")
    void getAllEmpty() {
        when(userRepository.findAll()).thenReturn(List.of()).thenReturn(List.of(user));

        List<User> userList = userService.getAll();

        assertEquals(0, userList.size());
        verify(userRepository, times(1)).findAll();

    }

    @Test
    @DisplayName("Get non empty user list")
    void getAllNotEmpty() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> userList = userService.getAll();

        assertEquals(1, userList.size());
        verify(userRepository, times(1)).findAll();

    }

    @Test
    @DisplayName("Load User by Username, user found")
    void loadUserByUsernameUserFound() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(this.user));

        User user = (User) userService.loadUserByUsername("user");

        assertEquals("user", user.getUsername());

        verify(userRepository, times(1)).findByUsername(anyString());
    }

    @Test
    @DisplayName("Load User by Username, user not found, throw UsernameNotFoundException")
    void loadUserByUsernameUserNotFoundThrowUsernameNotFoundException() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(anyString()));

        verify(userRepository, times(1)).findByUsername(anyString());
    }


}