package com.example.lib.service;

import com.example.lib.model.User;
import com.example.lib.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public String register(User user) {
        if (userRepo.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        user.setRole(user.getRole() != null ? user.getRole() : "CUSTOMER");
        userRepo.save(user);
        return "User registered successfully";
    }

    public String login(User user) {
        User existingUser = userRepo.findByUsername(user.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!existingUser.getPassword().equals(user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return "Login successful";
    }
}
