package com.example.wanderlust.web.filter;

import com.example.wanderlust.service.JwtTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtTokenService;
    private final List<RequestMatcher> shouldNotExecuteRequests;
    private final HttpStatusEntryPoint unauthorizedEntryPoint;

    @Autowired
    public JwtFilter(JwtTokenService jwtTokenService, HttpStatusEntryPoint unauthorizedEntryPoint) {

        this.jwtTokenService = jwtTokenService;
        this.unauthorizedEntryPoint = unauthorizedEntryPoint;
        shouldNotExecuteRequests =
                List.of(
                        new AntPathRequestMatcher("/login", "POST"),
                        new AntPathRequestMatcher("/users", "POST"));
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {

            String bearerPrefix = "Bearer";
            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

            Assert.notNull(authHeader, HttpHeaders.AUTHORIZATION + " header not found");
            Assert.isTrue(authHeader.startsWith(bearerPrefix), "Invalid " + HttpHeaders.AUTHORIZATION + " header");

            String jwt = authHeader.substring(bearerPrefix.length() + 1);
            UserDetails user = jwtTokenService.extractUserDetails(jwt);

            Authentication authenticated =
                    UsernamePasswordAuthenticationToken.authenticated(
                            user.getUsername(), jwt, user.getAuthorities());

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authenticated);
            SecurityContextHolder.setContext(context);

            request.setAttribute("TOKEN", Map.entry(user.getUsername(), jwt));

            if (request.getRequestURI().equals("/users") && request.getMethod().equals(HttpMethod.DELETE.name())) {
                blacklistJwtInRequestAttribute(request);
            }
        } catch (RuntimeException e) {

            unauthorizedEntryPoint.commence(request, response, new AuthenticationException(e.getMessage(), e) {
            });
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        return shouldNotExecuteRequests.stream().anyMatch(matcher -> matcher.matches(request));
    }

    public void blacklistJwtInRequestAttribute(HttpServletRequest request) {

        Assert.notNull(request, "Request cannot be null");
        Object attribute = request.getAttribute("TOKEN");

        if (attribute instanceof Map.Entry entry
                && entry.getKey() instanceof String && entry.getValue() instanceof String token) {

            jwtTokenService.blackListJwtToken(token);
        }
    }
}
