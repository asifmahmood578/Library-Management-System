package com.example.lib.service;

import com.example.lib.model.User;
import com.example.lib.repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepo userRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterSuccess() {
        User user = new User();
        user.setUsername("alice");
        user.setPassword("pass");
        user.setRole("CUSTOMER");

        when(userRepo.existsByUsername("alice")).thenReturn(false);

        String result = userService.register(user);

        assertEquals("User registered successfully", result);
        verify(userRepo, times(1)).save(user);
    }

    @Test
    void testRegisterDuplicateUsername() {
        User user = new User();
        user.setUsername("existing");

        when(userRepo.existsByUsername("existing")).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> userService.register(user));

        assertEquals("Username already exists", ex.getMessage());
    }

    @Test
    void testLoginSuccess() {
        User user = new User();
        user.setUsername("bob");
        user.setPassword("secret");

        User stored = new User();
        stored.setUsername("bob");
        stored.setPassword("secret");

        when(userRepo.findByUsername("bob")).thenReturn(Optional.of(stored));

        String result = userService.login(user);
        assertEquals("Login successful", result);
    }

    @Test
    void testLoginWrongPassword() {
        User user = new User();
        user.setUsername("bob");
        user.setPassword("wrong");

        User stored = new User();
        stored.setUsername("bob");
        stored.setPassword("secret");

        when(userRepo.findByUsername("bob")).thenReturn(Optional.of(stored));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> userService.login(user));

        assertEquals("Invalid password", ex.getMessage());
    }

    @Test
    void testLoginUserNotFound() {
        when(userRepo.findByUsername("ghost")).thenReturn(Optional.empty());

        User user = new User();
        user.setUsername("ghost");

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> userService.login(user));

        assertEquals("User not found", ex.getMessage());
    }
}
