package com.example.wanderlust.web.controller;

import com.example.wanderlust.exception.IllegalRequestException;
import com.example.wanderlust.service.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostAuthController {

    private final JwtTokenService jwtTokenService;

    @Autowired
    public PostAuthController(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    @PostMapping(path = "/login")
    public ResponseEntity<Void> postLogin(Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {

            throw new IllegalRequestException("Credentials needed");
        }

        String username = authentication.getName();
        String[] authorities;
        String jwt;
        ResponseEntity<Void> responseEntity;

        if (username == null) {
            throw new RuntimeException("Username is null");
        }

        authorities =
                authentication.getAuthorities().stream()
                        .map(
                                role -> {
                                    if (role != null) {
                                        return role.getAuthority();
                                    } else {
                                        throw new RuntimeException("GrantedAuthority is null for username: " + username);
                                    }
                                })
                        .toArray(String[]::new);

        UserDetails userDetails =
                org.springframework.security.core.userdetails.User.builder()
                        .username(username)
                        .password("")
                        .roles(authorities)
                        .build();

        jwt = jwtTokenService.createJwtToken(userDetails);

        responseEntity =
                ResponseEntity.accepted().header("token", jwt).build();

        return responseEntity;
    }
}
