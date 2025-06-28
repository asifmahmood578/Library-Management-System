package com.example.lib.service;

import com.example.lib.exception.DuplicateUsernameException;
import com.example.lib.exception.InvalidCredentialsException;
import com.example.lib.exception.UserNotFoundException;
import com.example.lib.model.User;
import com.example.lib.repository.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepo userRepo;

    public String register(User user) {
        logger.info("Registering user: {}", user.getUsername());

        if (userRepo.existsByUsername(user.getUsername())) {
            logger.warn("Username already exists: {}", user.getUsername());
            throw new DuplicateUsernameException("Username already exists: " + user.getUsername());
        }

        user.setRole(user.getRole() != null ? user.getRole() : "CUSTOMER");
        userRepo.save(user);
        logger.info("User registered successfully: {}", user.getUsername());

        return "User registered successfully";
    }

    public String login(User user) {
        logger.info("User login attempt: {}", user.getUsername());

        User existingUser = userRepo.findByUsername(user.getUsername())
                .orElseThrow(() -> {
                    logger.error("User not found: {}", user.getUsername());
                    return new UserNotFoundException("User not found: " + user.getUsername());
                });

        if (!existingUser.getPassword().equals(user.getPassword())) {
            logger.error("Invalid password for user: {}", user.getUsername());
            throw new InvalidCredentialsException("Invalid password for user: " + user.getUsername());
        }

        logger.info("User login successful: {}", user.getUsername());
        return "Login successful";
    }
}
