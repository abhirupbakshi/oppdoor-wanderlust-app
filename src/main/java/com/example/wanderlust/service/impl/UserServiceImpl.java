package com.example.wanderlust.service.impl;

import com.example.wanderlust.exception.UserAbsentException;
import com.example.wanderlust.exception.UserExistException;
import com.example.wanderlust.model.User;
import com.example.wanderlust.repository.UserRepository;
import com.example.wanderlust.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<User> getUserByUsername(String username) {

        Assert.notNull(username, "Username cannot be null");

        return userRepository.findById(username);
    }

    @Override
    public User createUser(User user) {

        Assert.notNull(user, "User cannot be null");

        if (userRepository.existsById(user.getUsername())) {
            throw new UserExistException("User with username: " + user.getUsername() + " already exist");
        }

        user
                .setPassword(passwordEncoder.encode(user.getPassword()))
                .setRoles(new ArrayList<>(List.of("USER")))
                .setAccountNonExpired(true)
                .setAccountNonLocked(true)
                .setCredentialsNonExpired(true)
                .setEnabled(true);

        return userRepository.save(user);
    }

    @Override
    public User editUser(User user) {

        throw new RuntimeException("Method not implemented");
    }

    @Override
    public User deleteUser(String username) {

        Assert.notNull(username, "Username cannot be null");

        User fetchedUser = userRepository.findById(username)
                .orElseThrow(() -> new UserAbsentException("User with username: " + username + " does not exist"));

        userRepository.deleteById(username);
        return fetchedUser;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username: " + username + " does not exist"));
    }
}
