package com.example.personalfinance.service;

import com.example.personalfinance.dto.UserRegistrationRequest;
import com.example.personalfinance.entity.User;
import com.example.personalfinance.exception.UserAlreadyExistsException;
import com.example.personalfinance.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for UserService
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private UserRegistrationRequest registrationRequest;

    @BeforeEach
    void setUp() {
        registrationRequest = new UserRegistrationRequest();
        registrationRequest.setUsername("test@example.com");
        registrationRequest.setPassword("password123");
        registrationRequest.setFullName("Test User");
        registrationRequest.setPhoneNumber("+1234567890");
    }

    @Test
    void registerUser_Success() {
        // Given
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });

        // When
        User result = userService.registerUser(registrationRequest);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("test@example.com", result.getUsername());
        assertEquals("encodedPassword", result.getPassword());
        assertEquals("Test User", result.getFullName());
        assertEquals("+1234567890", result.getPhoneNumber());

        verify(userRepository).existsByUsername("test@example.com");
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerUser_UserAlreadyExists() {
        // Given
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        // When & Then
        UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class, () -> {
            userService.registerUser(registrationRequest);
        });

        assertEquals("User with username test@example.com already exists", exception.getMessage());
        verify(userRepository).existsByUsername("test@example.com");
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void findByUsername_Success() {
        // Given
        User user = new User();
        user.setUsername("test@example.com");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        // When
        User result = userService.findByUsername("test@example.com");

        // Then
        assertNotNull(result);
        assertEquals("test@example.com", result.getUsername());
        verify(userRepository).findByUsername("test@example.com");
    }

    @Test
    void findByUsername_NotFound() {
        // Given
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // When
        User result = userService.findByUsername("test@example.com");

        // Then
        assertNull(result);
        verify(userRepository).findByUsername("test@example.com");
    }
}
