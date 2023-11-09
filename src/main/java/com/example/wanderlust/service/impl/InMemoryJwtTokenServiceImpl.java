package com.example.wanderlust.service.impl;

import com.example.wanderlust.service.JwtTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InMemoryJwtTokenServiceImpl implements JwtTokenService {

    private final long jwtExpirationSeconds;
    private final Key jwtSigningKey;
    private final String userRoleDelimiter = ":";
    private final String jwtUserRoleClaimFieldName = "roles";
    private final String jwtAccountExpiredFieldName = "isAccountExpired";
    private final String jwtAccountLockedFieldName = "isAccountLocked";
    private final String jwtCredentialsExpiredFieldName = "isCredentialsExpired";
    private final Set<String> blacklisted = new HashSet<>();

    @Autowired
    public InMemoryJwtTokenServiceImpl(
            @Value("${JWT_EXPIRATION_SECONDS}") long jwtExpirationSeconds,
            @Value("${JWT_KEY}") String key) {

        this.jwtExpirationSeconds = jwtExpirationSeconds;
        this.jwtSigningKey = Keys.hmacShaKeyFor(key.getBytes());
    }

    @Override
    public boolean isJwtTokenBlacklisted(String jwt) {

        return blacklisted.contains(jwt);
    }

    @Override
    public String createJwtToken(UserDetails userDetails) {

        Assert.notNull(userDetails, "User must not be null");

        String authorities = userDetails.getAuthorities() == null ?
                "" :
                userDetails
                        .getAuthorities()
                        .stream()
                        .map(role -> role.getAuthority().split("_")[1])
                        .collect(Collectors.joining(userRoleDelimiter));

        String jwt = Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim(jwtUserRoleClaimFieldName, authorities)
                .claim(jwtAccountExpiredFieldName, String.valueOf(!userDetails.isAccountNonExpired()))
                .claim(jwtAccountLockedFieldName, String.valueOf(!userDetails.isAccountNonLocked()))
                .claim(jwtCredentialsExpiredFieldName, String.valueOf(!userDetails.isCredentialsNonExpired()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationSeconds * 1000))
                .signWith(jwtSigningKey)
                .compact();

        return jwt;
    }

    @Override
    public void blackListJwtToken(String jwt) {

        Assert.notNull(jwt, "JWT must not be null");

        blacklisted.add(jwt);
    }

    @Override
    public UserDetails extractUserDetails(String jwt) {

        Assert.notNull(jwt, "JWT must not be null");

        Jws<Claims> jws = Jwts
                .parserBuilder()
                .setSigningKey(jwtSigningKey)
                .build()
                .parseClaimsJws(jwt);

        List<String> roles = Arrays
                .stream(jws
                        .getBody()
                        .get(jwtUserRoleClaimFieldName, String.class)
                        .split(userRoleDelimiter)
                )
                .toList();

        UserDetails user = User.builder()
                .password(jwt)
                .username(jws.getBody().getSubject())
                .roles(roles.toArray(new String[0]))
                .accountExpired(Boolean.parseBoolean(jws.getBody().get(jwtAccountExpiredFieldName, String.class)))
                .accountLocked(Boolean.parseBoolean(jws.getBody().get(jwtAccountLockedFieldName, String.class)))
                .credentialsExpired(Boolean.parseBoolean(jws.getBody().get(jwtCredentialsExpiredFieldName, String.class)))
                .build();

        if (isJwtTokenBlacklisted(jwt)) {
            throw new JwtException("JWT token is invalid");
        }

        return user;
    }
}
