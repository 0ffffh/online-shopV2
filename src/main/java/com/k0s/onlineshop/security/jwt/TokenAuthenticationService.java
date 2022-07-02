package com.k0s.onlineshop.security.jwt;

import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;


@Service
@Slf4j
public class TokenAuthenticationService {

    private static final String ACCESS_TOKEN = "access_token";
    private static final String REFRESH_TOKEN = "refresh_token";


    private final JWTTokenizer jwtTokenizer;

    @Autowired
    public TokenAuthenticationService(JWTTokenizer jwtTokenizer) {
        this.jwtTokenizer = jwtTokenizer;
    }

    public void addAuthentication(HttpServletResponse response, Authentication authentication) {

        setCookie(ACCESS_TOKEN, jwtTokenizer.createAccessToken(authentication), response);
        setCookie(REFRESH_TOKEN, jwtTokenizer.createRefreshToken(authentication), response);

    }


    public Authentication getAuthentication(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        Cookie[] cookies = httpRequest.getCookies();


        Optional<Cookie> accessTokenCookie = Arrays.stream(Optional.ofNullable(cookies).
                        orElse(new Cookie[0]))
                .filter(cookie -> ("access_token").equals(cookie.getName())).findFirst();

        Optional<Cookie> refreshTokenCookie = Arrays.stream(Optional.ofNullable(cookies).
                        orElse(new Cookie[0]))
                .filter(cookie -> ("refresh_token").equals(cookie.getName())).findFirst();


        if (accessTokenCookie.isEmpty() || refreshTokenCookie.isEmpty()) {
            return null;
        }

        String accessToken = accessTokenCookie.get().getValue();
        String refreshToken = refreshTokenCookie.get().getValue();

        try {

            return jwtTokenizer.getAuthenticationFromAccessToken(accessToken);

        } catch (JWTVerificationException e) {
            log.info("FAIL ACCESS TOKEN {} : ", accessToken, e);

            try {

                log.info("Authenticating with REFRESH TOKEN: ");
                Authentication authentication = jwtTokenizer.getAuthenticationFromRefreshToken(refreshToken);

                setCookie(ACCESS_TOKEN, jwtTokenizer.createAccessToken(authentication), httpResponse);

                log.info("Authenticated user : {} roles : {}", authentication.getName(), authentication.getAuthorities());
                return authentication;

            } catch (JWTVerificationException ex) {
                log.info("FAIL REFRESH TOKEN {} : ", refreshToken, e);
                return null;
            }
        }
    }

    private void setCookie(String name, String value, HttpServletResponse response){
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

}
