package com.example.personalfinance.repository;

import com.example.personalfinance.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for UserRepository
 */
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("test@example.com");
        user.setPassword("encodedPassword");
        user.setFullName("Test User");
        user.setPhoneNumber("+1234567890");
    }

    @Test
    void findByUsername_Success() {
        // Given
        entityManager.persistAndFlush(user);

        // When
        Optional<User> result = userRepository.findByUsername("test@example.com");

        // Then
        assertTrue(result.isPresent());
        assertEquals("test@example.com", result.get().getUsername());
        assertEquals("Test User", result.get().getFullName());
    }

    @Test
    void findByUsername_NotFound() {
        // When
        Optional<User> result = userRepository.findByUsername("nonexistent@example.com");

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    void existsByUsername_True() {
        // Given
        entityManager.persistAndFlush(user);

        // When
        boolean exists = userRepository.existsByUsername("test@example.com");

        // Then
        assertTrue(exists);
    }

    @Test
    void existsByUsername_False() {
        // When
        boolean exists = userRepository.existsByUsername("nonexistent@example.com");

        // Then
        assertFalse(exists);
    }

    @Test
    void save_Success() {
        // When
        User savedUser = userRepository.save(user);

        // Then
        assertNotNull(savedUser.getId());
        assertEquals("test@example.com", savedUser.getUsername());
        assertEquals("Test User", savedUser.getFullName());
        assertNotNull(savedUser.getCreatedAt());
    }
}
