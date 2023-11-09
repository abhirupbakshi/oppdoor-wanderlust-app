package com.example.todo.web.authentication;

import com.example.todo.configuration.ConstantValues;
import com.example.todo.service.JwtTokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

  private final JwtTokenService jwtTokenService;

  @Autowired
  public CustomLogoutSuccessHandler(JwtTokenService jwtTokenService) {
    this.jwtTokenService = jwtTokenService;
  }

  // This logic is repeated in the jwt filter also.
  @Override
  public void onLogoutSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException, ServletException {

    log.debug("Parameter:: request: {}", request);

    if (request == null) throw new RuntimeException("Request cannot be null");
    log.debug("Not null check passed for request");

    Object attribute = request.getAttribute(ConstantValues.Jwt.REQUEST_ATTRIBUTE_TOKEN_KEY);
    log.debug(
        "Extracted request attribute {} = {}",
        ConstantValues.Jwt.REQUEST_ATTRIBUTE_TOKEN_KEY,
        attribute);

    if (attribute instanceof Map.Entry entry
        && entry.getKey() instanceof String username
        && entry.getValue() instanceof String token) {
      jwtTokenService.blackListJwtToken(token);
      log.info("Token {} blacklisted for username {}", token, username);
    } else {
      log.info("No token was blacklisted");
    }
    log.info("JWT token blacklisted at request url: {}", request.getRequestURL());
  }
}
