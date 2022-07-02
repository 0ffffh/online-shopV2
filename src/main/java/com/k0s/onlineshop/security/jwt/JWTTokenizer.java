package com.k0s.onlineshop.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.k0s.onlineshop.security.user.Role;
import com.k0s.onlineshop.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class JWTTokenizer {
    private static final String ROLES = "roles";


    private final UserService userService;

    @Autowired
    public JWTTokenizer(UserService userService) {
        this.userService = userService;
    }

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access.token.expires}")
    private int accessTokenExpires;

    @Value("${jwt.refresh.token.expires}")
    private int refreshTokenExpires;

    private Algorithm algorithm;

    private JWTVerifier verifier;

    @PostConstruct
    private void init() {
        algorithm = Algorithm.HMAC256(secret.getBytes());
        verifier = JWT.require(algorithm).build();
    }


    public UsernamePasswordAuthenticationToken getAuthenticationFromAccessToken(String token) {
        DecodedJWT decode = verifier.verify(token);

        String username = decode.getSubject();
        List<Role> roleList = decode.getClaim("roles").asList(Role.class);


        log.info("Parse Access TOKEN {}, user : {}, roles : {}", token, username, roleList);
        return new UsernamePasswordAuthenticationToken(username, null, roleList);
    }

    public UsernamePasswordAuthenticationToken getAuthenticationFromRefreshToken(String token) {

        UserDetails user = userService.loadUserByUsername(verifier.verify(token).getSubject());

        log.info("Parse Refresh TOKEN {}, user : {}, roles : {}", token, user.getUsername(), user.getAuthorities());

        return new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
    }

    public String createRefreshToken(Authentication authentication) {
        log.info("Create Access token for user : {}", authentication.getName());
        return JWT.create()
                .withSubject(authentication.getName())
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshTokenExpires))
                .sign(algorithm);
    }

    public String createAccessToken(Authentication authentication) {
        log.info("Create Access token for user : {}", authentication.getName());

        return JWT.create()
                .withSubject(authentication.getName())
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenExpires))
                .withClaim(ROLES, authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .sign(algorithm);
    }


}
