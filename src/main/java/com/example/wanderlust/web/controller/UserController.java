package com.example.wanderlust.web.controller;

import com.example.wanderlust.exception.HttpMethodNotImplementedException;
import com.example.wanderlust.exception.UserAbsentException;
import com.example.wanderlust.model.User;
import com.example.wanderlust.model.validation.group.Create;
import com.example.wanderlust.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {

        this.userService = userService;
    }

    @GetMapping
    ResponseEntity<User> getUserByUsername(Authentication authentication) {

        String username = authentication.getName();
        Optional<User> optionalUser = userService.getUserByUsername(username);

        if (optionalUser.isEmpty()) {
            throw new UserAbsentException("User with username: " + username + " does not exist");
        }

        return ResponseEntity.ok(optionalUser.get());
    }

    @PostMapping
    ResponseEntity<User> createUser(@Validated(Create.class) @RequestBody User user, HttpServletRequest request) {

        user = userService.createUser(user);

        return ResponseEntity.created(URI.create(request.getRequestURI())).body(user);
    }

    @PutMapping
    ResponseEntity<User> editUser() {

        throw new HttpMethodNotImplementedException("Edit user http method has not been implemented yet");
    }

    @DeleteMapping
    ResponseEntity<User> deleteUser(Authentication authentication) {

        String username = authentication.getName();
        User deletedUser = userService.deleteUser(username);

        return ResponseEntity.ok(deletedUser);
    }
}
