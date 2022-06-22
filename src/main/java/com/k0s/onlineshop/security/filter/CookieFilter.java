package com.k0s.onlineshop.security.filter;

import com.k0s.onlineshop.security.jwt.TokenAuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.Optional;

@Slf4j
public class CookieFilter extends GenericFilterBean {
    private final TokenAuthenticationService tokenAuthenticationService;

    @Autowired
    public CookieFilter(TokenAuthenticationService tokenAuthenticationService) {
        this.tokenAuthenticationService = tokenAuthenticationService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        Optional<Authentication> authentication = Optional.ofNullable(tokenAuthenticationService.getAuthentication(request, response));
        authentication.ifPresent(auth -> SecurityContextHolder.getContext().setAuthentication(auth));

        chain.doFilter(request, response);
    }

}
