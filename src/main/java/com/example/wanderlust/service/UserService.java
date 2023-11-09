package com.example.wanderlust.service;

import com.example.wanderlust.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends UserDetailsService {

    Optional<User> getUserByUsername(String username);

    User createUser(User user);

    User editUser(User user);

    User deleteUser(String username);
}
