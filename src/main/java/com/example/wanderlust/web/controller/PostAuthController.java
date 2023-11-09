package com.example.todo.web.controller;

import com.example.todo.configuration.ConstantValues;
import com.example.todo.exception.IllegalRequestException;
import com.example.todo.model.User;
import com.example.todo.service.JwtTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(ConstantValues.RestApi.REST_API_PREFIX + "/auth")
public class PostAuthController {

  private final JwtTokenService jwtTokenService;

  @Autowired
  public PostAuthController(JwtTokenService jwtTokenService) {
    this.jwtTokenService = jwtTokenService;
  }

  /**
   * This method does all the post-login operations for a {@link User}. The user with given
   * credentials is thought to be validated by spring security and thus the {@link Authentication}'s
   * user details are directly used for the post-login operations. <br>
   * <br>
   * In current implementation:
   *
   * <ul>
   *   <li>It creates a JWT token from the created {@link Authentication} and adds it to the
   *       response header.
   * </ul>
   *
   * @param authentication The created {@link Authentication} from the already authenticated {@link
   *     User}
   * @return A {@link ResponseEntity}<{@link Void}> containing the created JWT token in the header.
   * @throws IllegalRequestException If the authentication is null or the user is not authenticated.
   * @throws RuntimeException If the extracted username from the {@link Authentication} is null or
   *     any of the {@link GrantedAuthority} is null for the user.
   * @see ConstantValues.Jwt
   */
  @PostMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> postLogin(Authentication authentication) {

    if (authentication == null || !authentication.isAuthenticated()) {
      throw new IllegalRequestException(ConstantValues.User.Error.CREDENTIALS_NEEDED);
    }
    log.debug("Not null check passed for authentication parameter");

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
                    // Separating the "ROLE_" prefix from the user authorities/roles
                    return role.getAuthority().split("_")[1];
                  } else {
                    throw new RuntimeException(
                        "GrantedAuthority is null for username: " + username);
                  }
                })
            .toArray(String[]::new);

    UserDetails userDetails =
        org.springframework.security.core.userdetails.User.builder()
            .username(username)
            .password("")
            .roles(authorities)
            .build();
    log.debug("Created UserDetails: {}", userDetails);

    jwt = jwtTokenService.createJwtToken(userDetails);
    log.debug("Created jwt: {}", jwt);

    responseEntity =
        ResponseEntity.accepted().header(ConstantValues.Jwt.RESPONSE_HEADER_TOKEN, jwt).build();
    log.debug("Added response header {}:{}", ConstantValues.Jwt.RESPONSE_HEADER_TOKEN, jwt);

    log.info("Login successful for user with username: {}", username);

    return responseEntity;
  }
}
