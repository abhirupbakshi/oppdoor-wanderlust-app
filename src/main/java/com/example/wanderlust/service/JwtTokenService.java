package com.example.todo.service;

import org.springframework.security.core.userdetails.UserDetails;
import io.jsonwebtoken.JwtException;

/**
 * This interface provides services that are related to JWT token.
 * The implementation can use any storage technology (if needed) under the hood.
 * This service is assumed to be able to work in isolation, independent of other services or sored data.
 */
public interface JwtTokenService {

    /**
     * Creates and returns a JWT token of type {@link String} from a given {@link UserDetails}.
     * @param userDetails The {@link UserDetails} of a user.
     * @return The JWT token of type {@link String}.
     * @throws IllegalArgumentException If the given {@link UserDetails} parameter is null
     */
    String createJwtToken(UserDetails userDetails);

    /**
     * Extracts user information and returns a {@link UserDetails} from a given JWT token of type {@link String}.
     * @param jwt The JWT token of type {@link String}.
     * @return The created {@link UserDetails} from the given JWT.
     * @throws IllegalArgumentException If the given JWT token is null.
     * @throws JwtException Or any child of it, if the given JWT token is invalid or if it is blacklisted.
     * @see JwtTokenService#blackListJwtToken(String)
     */
    UserDetails extractUserDetails(String jwt);

    /**
     * Given a JWT token of type {@link String}, it blacklists/invalidates the JWT token.
     * It does not check for any validity of the token.
     * @param jwt The JWT token of type {@link String}.
     * @throws IllegalArgumentException If the given JWT token is null.
     */
    void blackListJwtToken(String jwt);

    /**
     * Given a JWT of type {@link String}, it checks whether the JWT is already blacklisted or not.
     * Returns true if it is, else false.
     * It does not check for any validity of the token.
     * @param jwt The jwt token of type {@link String}.
     * @return A {@code boolean} value indicating whether the JWT is already blacklisted or not.
     * @throws IllegalArgumentException If the given JWT token is null.
     */
    boolean isJwtTokenBlacklisted(String jwt);
}
