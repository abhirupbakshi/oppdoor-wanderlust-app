package com.example.wanderlust.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends MainConfig {

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .authorizeHttpRequests(registry ->
                        registry
                                .requestMatchers("/users").permitAll()
                                .anyRequest().authenticated()
                )
                .csrf(CsrfConfigurer::disable)
                .cors(CorsConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(basic -> basic.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.FORBIDDEN)))
                .build();
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }
}
