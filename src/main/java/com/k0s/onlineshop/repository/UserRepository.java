package com.k0s.onlineshop.repository;

import com.k0s.onlineshop.security.user.AuthUser;
import com.k0s.onlineshop.security.user.Role;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<AuthUser> findByUsername(String username);
    List<AuthUser> getAll();

    List<Role> getUserRolesByUsername(String username);

}
