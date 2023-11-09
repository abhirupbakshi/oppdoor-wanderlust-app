package com.example.todo.service.implementation;

import com.example.todo.configuration.ConstantValues;
import com.example.todo.service.JwtTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This implementation of {@link JwtTokenService} stores JWT tokens in in-memory data structure.
 * It is only meant to be used for development/testing and not in production.
 */
@Component
@Slf4j
public class InMemoryJwtTokenServiceImpl implements JwtTokenService {

    private final String userRoleDelimiter;
    private final String jwtUserRoleClaimFieldName;
    private final String jwtAccountExpiredFieldName;
    private final String jwtAccountLockedFieldName;
    private final String jwtCredentialsExpiredFieldName;
    private final long jwtExpirationSeconds;
    private final Key jwtSigningKey;
    private final Set<String> blacklisted = new HashSet<>();

    @Autowired
    public InMemoryJwtTokenServiceImpl(@Qualifier("userRoleDelimiter") String userRoleDelimiter,
                                       @Qualifier("jwtUserRoleClaimFieldName") String jwtUserRoleClaimFieldName,
                                       @Qualifier("jwtAccountExpiredFieldName")String jwtAccountExpiredFieldName,
                                       @Qualifier("jwtAccountLockedFieldName")String jwtAccountLockedFieldName,
                                       @Qualifier("jwtCredentialsExpiredFieldName")String jwtCredentialsExpiredFieldName,
                                       @Qualifier("jwtExpirationSeconds") long jwtExpirationSeconds,
                                       Key jwtSigningKey) {
        this.userRoleDelimiter = userRoleDelimiter;
        this.jwtExpirationSeconds = jwtExpirationSeconds;
        this.jwtUserRoleClaimFieldName = jwtUserRoleClaimFieldName;
        this.jwtSigningKey = jwtSigningKey;
        this.jwtAccountExpiredFieldName = jwtAccountExpiredFieldName;
        this.jwtAccountLockedFieldName = jwtAccountLockedFieldName;
        this.jwtCredentialsExpiredFieldName = jwtCredentialsExpiredFieldName;
    }

    @Override
    public boolean isJwtTokenBlacklisted(String jwt) {
        log.debug("Parameters:: jwt: {}", jwt);
        return blacklisted.contains(jwt);
    }

    @Override
    public String createJwtToken(UserDetails userDetails) {

        log.debug("Parameters:: user: {}", userDetails);

        Assert.notNull(userDetails, "User must not be null");
        log.debug("Not null check passed for user parameter");

        String authorities = userDetails.getAuthorities() == null ?
                "" :
                userDetails
                        .getAuthorities()
                        .stream()
                        .map(role -> role.getAuthority().split("_")[1])
                        .collect(Collectors.joining(userRoleDelimiter));
        log.debug("Generated authorities: {}", authorities);

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
        log.info("Generated JWT: {} for user: {}", jwt, userDetails);

        return jwt;
    }

    @Override
    public void blackListJwtToken(String jwt) {

        log.debug("Parameters:: jwt: {}", jwt);

        Assert.notNull(jwt, "JWT must not be null");
        log.debug("Not null check passed for jwt parameter");

        blacklisted.add(jwt);

        log.info("Blacklisting of token: {} is successful", jwt);
    }

    @Override
    public UserDetails extractUserDetails(String jwt) {

        log.debug("Parameters:: jwt: {}", jwt);

        Assert.notNull(jwt, "JWT must not be null");
        log.debug("Not null check passed for jwt parameter");

        Jws<Claims> jws = Jwts
                .parserBuilder()
                .setSigningKey(jwtSigningKey)
                .build()
                .parseClaimsJws(jwt);
        log.debug("Parsed JWS: {}", jws);

        List<String> roles = Arrays
                .stream(jws
                        .getBody()
                        .get(jwtUserRoleClaimFieldName, String.class)
                        .split(userRoleDelimiter)
                )
                .toList();
        log.debug("Parsed roles/authorities: {}", roles);

        UserDetails user = User.builder()
                .password(jwt)
                .username(jws.getBody().getSubject())
                .roles(roles.toArray(new String[0]))
                .accountExpired(Boolean.parseBoolean(jws.getBody().get(jwtAccountExpiredFieldName, String.class)))
                .accountLocked(Boolean.parseBoolean(jws.getBody().get(jwtAccountLockedFieldName, String.class)))
                .credentialsExpired(Boolean.parseBoolean(jws.getBody().get(jwtCredentialsExpiredFieldName, String.class)))
                .build();
        log.info("Parsed user: {}", user);

        if (isJwtTokenBlacklisted(jwt)) {
            log.debug("JWT is blacklisted");
            throw new JwtException(ConstantValues.Jwt.Error.INVALID_TOKEN);
        }

        return user;
    }
}
