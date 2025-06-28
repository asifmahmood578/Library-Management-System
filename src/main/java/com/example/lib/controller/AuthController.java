package com.example.lib.controller;

import com.example.lib.model.User;
import com.example.lib.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid User user) {
        String response = userService.register(user);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid User user) {
        String response = userService.login(user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}