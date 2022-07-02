package com.k0s.onlineshop.service;

import com.k0s.onlineshop.repository.UserRepository;
import com.k0s.onlineshop.security.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    @Autowired
    private final UserRepository userRepository;


    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            log.info("User {} not found in database", username);
            throw new UsernameNotFoundException("User " + username + " not found");
        }

        log.info("User {} found in database", username);
        return user.get();
    }

    public User save(User user) {
        return userRepository.save(user);
    }
}




