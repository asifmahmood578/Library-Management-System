package com.example.lib.controller;

import com.example.lib.model.User;
import com.example.lib.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("testuser");
        user.setPassword("1234");
        user.setRole("CUSTOMER");
    }

    @Test
    void testRegisterSuccess() throws Exception {
        when(userService.register(any(User.class))).thenReturn("User registered successfully");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testuser\",\"password\":\"1234\",\"role\":\"CUSTOMER\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().string("User registered successfully"));
    }

    @Test
    void testLoginSuccess() throws Exception {
        when(userService.login(any(User.class))).thenReturn("Login successful");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testuser\",\"password\":\"1234\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Login successful"));
    }
}


