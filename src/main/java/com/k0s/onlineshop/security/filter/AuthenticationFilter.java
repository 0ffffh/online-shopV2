package com.k0s.onlineshop.security.filter;

import com.k0s.onlineshop.security.jwt.TokenAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final TokenAuthenticationService tokenAuthenticationService;

    @Autowired
    public AuthenticationFilter(TokenAuthenticationService tokenAuthenticationService) {
        this.tokenAuthenticationService = tokenAuthenticationService;
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {

        tokenAuthenticationService.addAuthentication(response, authentication);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        super.getSuccessHandler().onAuthenticationSuccess(request, response, authentication);
        chain.doFilter(request, response);

    }
}
