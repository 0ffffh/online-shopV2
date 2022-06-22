package com.k0s.onlineshop.service;


import com.k0s.onlineshop.repository.UserRepository;
import com.k0s.onlineshop.security.user.AuthUser;
import com.k0s.onlineshop.security.user.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    @Autowired
    private final UserRepository userRepository;

    public List<AuthUser> getAll() {
        List<AuthUser> authUserList = userRepository.getAll();
        authUserList.forEach(user -> user.setRoles(new HashSet<>(getUserRolesByUsername(user.getUsername()))));
        return authUserList;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AuthUser> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            log.info("AuthUser {} not found in database", username);
            throw new UsernameNotFoundException("AuthUser " + username + " not found");
        }

        log.info("AuthUser {} found in database", username);
        user.get().setRoles(getUserRolesByUsername(username));
        return user.get();
    }

    protected List<Role> getUserRolesByUsername(String username) {
        return userRepository.getUserRolesByUsername(username);
    }
}




