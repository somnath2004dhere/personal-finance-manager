package com.example.personalfinance.service;

import com.example.personalfinance.dto.UserRegistrationRequest;
import com.example.personalfinance.entity.User;
import com.example.personalfinance.exception.UserAlreadyExistsException;
import com.example.personalfinance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service class for user management operations
 */
@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    /**
     * Register a new user
     */
    public User registerUser(UserRegistrationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("User with username " + request.getUsername() + " already exists");
        }
        
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());
        
        return userRepository.save(user);
    }
    
    /**
     * Find user by username
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
}
