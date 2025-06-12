package com.example.personalfinance.controller;

import com.example.personalfinance.dto.UserLoginRequest;
import com.example.personalfinance.dto.UserRegistrationRequest;
import com.example.personalfinance.entity.User;
import com.example.personalfinance.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for AuthController
 */
@WebMvcTest(AuthController.class)
@Import({com.example.personalfinance.config.SecurityConfig.class})
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private com.example.personalfinance.service.CustomUserDetailsService customUserDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserRegistrationRequest registrationRequest;
    private UserLoginRequest loginRequest;
    private User user;

    @BeforeEach
    void setUp() {
        registrationRequest = new UserRegistrationRequest();
        registrationRequest.setUsername("test@example.com");
        registrationRequest.setPassword("password123");
        registrationRequest.setFullName("Test User");
        registrationRequest.setPhoneNumber("+1234567890");

        loginRequest = new UserLoginRequest();
        loginRequest.setUsername("test@example.com");
        loginRequest.setPassword("password123");

        user = new User();
        user.setId(1L);
        user.setUsername("test@example.com");
        user.setFullName("Test User");
    }

    @Test
    void register_Success() throws Exception {
        // Given
        when(userService.registerUser(any(UserRegistrationRequest.class))).thenReturn(user);

        // When & Then
        mockMvc.perform(post("/auth/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("User registered successfully"))
                .andExpect(jsonPath("$.userId").value(1));
    }

    @Test
    void register_ValidationError() throws Exception {
        // Given
        registrationRequest.setUsername("invalid-email");

        // When & Then
        mockMvc.perform(post("/auth/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_EmptyFullName() throws Exception {
        // Given
        registrationRequest.setFullName("");

        // When & Then
        mockMvc.perform(post("/auth/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fullName").exists());
    }

    @Test
    void register_InvalidPhoneNumber() throws Exception {
        // Given
        registrationRequest.setPhoneNumber("invalid-phone");

        // When & Then
        mockMvc.perform(post("/auth/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.phoneNumber").exists());
    }

    @Test
    void login_Success() throws Exception {
        // Given
        Authentication auth = new UsernamePasswordAuthenticationToken("test@example.com", "password123");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);

        // When & Then
        mockMvc.perform(post("/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login successful"));
    }

    @Test
    void login_EmptyUsername() throws Exception {
        // Given
        loginRequest.setUsername("");

        // When & Then
        mockMvc.perform(post("/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.username").exists());
    }

    @Test
    void login_EmptyPassword() throws Exception {
        // Given
        loginRequest.setPassword("");

        // When & Then
        mockMvc.perform(post("/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.password").exists());
    }

    @Test
    @WithMockUser
    void logout_Success() throws Exception {
        // When & Then
        mockMvc.perform(post("/auth/logout")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Logout successful"));
    }
}
