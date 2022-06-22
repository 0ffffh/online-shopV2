package com.k0s.onlineshop.security;

import com.k0s.onlineshop.security.jwt.TokenAuthenticationService;
import com.k0s.onlineshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@Profile(value = "dev")
public class ApplicationSecurityConfig {


//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private TokenAuthenticationService tokenAuthenticationService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.httpBasic().disable();
        http
                .csrf().disable();
//        http
//                .csrf().disable()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//        http
//                .addFilterBefore(new CookieFilter(tokenAuthenticationService), UsernamePasswordAuthenticationFilter.class)
//                .addFilterAt(new AuthenticationFilter(tokenAuthenticationService), UsernamePasswordAuthenticationFilter.class);
//        http
//                .authorizeRequests()
//                .antMatchers("/", "/login", "/logout").permitAll()
//                .antMatchers("/admin/**").hasRole("ADMIN")
//                .antMatchers("/user/**").hasRole("USER");

//                .and()
//                .httpBasic();
        http
//                .authorizeRequests().anyRequest().permitAll();
                .authorizeRequests().antMatchers("/").permitAll();
        return http.build();
    }


//
//    @Bean
//    protected UserDetailsService userDetailsService(){
//        return userService;
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder(12);
//    }
}
