package com.k0s.onlineshop.service;

import com.k0s.onlineshop.repository.UserRepository;
import com.k0s.onlineshop.security.user.AuthUser;
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
    private AuthUser authUser;

    @BeforeEach
    void setUp() {
        authUser = new AuthUser();
        authUser.setUsername("authUser");
    }

    @Test
    @DisplayName("Get empty authUser list")
    void getAllEmpty() {
        when(userRepository.getAll()).thenReturn(List.of()).thenReturn(List.of(authUser));

        List<AuthUser> authUserList = userService.getAll();

        assertEquals(0, authUserList.size());
        verify(userRepository, times(1)).getAll();
        verify(userRepository, never()).getUserRolesByUsername(anyString());

    }

    @Test
    @DisplayName("Get non empty authUser list")
    void getAllNotEmpty() {
        when(userRepository.getAll()).thenReturn(List.of(authUser));

        List<AuthUser> authUserList = userService.getAll();

        assertEquals(1, authUserList.size());
        verify(userRepository, times(1)).getAll();
        verify(userRepository, atLeastOnce()).getUserRolesByUsername(anyString());

    }

    @Test
    @DisplayName("Load AuthUser by Username, authUser found")
    void loadUserByUsernameUserFound() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(this.authUser));

        AuthUser authUser = (AuthUser) userService.loadUserByUsername("authUser");

        assertEquals("authUser", authUser.getUsername());

        verify(userRepository, times(1)).findByUsername(anyString());
        verify(userRepository, atLeastOnce()).getUserRolesByUsername(anyString());
    }

    @Test
    @DisplayName("Load AuthUser by Username, authUser not found, throw UsernameNotFoundException")
    void loadUserByUsernameUserNotFoundThrowUsernameNotFoundException() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(anyString()));

        verify(userRepository, times(1)).findByUsername(anyString());
        verify(userRepository, never()).getUserRolesByUsername(anyString());
    }


}