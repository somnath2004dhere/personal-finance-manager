package com.example.personalfinance.controller;

import com.example.personalfinance.dto.SavingsGoalRequest;
import com.example.personalfinance.dto.SavingsGoalUpdateRequest;
import com.example.personalfinance.entity.SavingsGoal;
import com.example.personalfinance.entity.User;
import com.example.personalfinance.service.SavingsGoalService;
import com.example.personalfinance.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for savings goal operations
 */
@RestController
@RequestMapping("/goals")
public class SavingsGoalController {
    
    @Autowired
    private SavingsGoalService savingsGoalService;
    
    @Autowired
    private UserService userService;
    
    /**
     * Create a new savings goal
     */
    @PostMapping
    public ResponseEntity<SavingsGoal> createGoal(@Valid @RequestBody SavingsGoalRequest request, Authentication auth) {
        User user = userService.findByUsername(auth.getName());
        SavingsGoal goal = savingsGoalService.createGoal(request, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(goal);
    }
    
    /**
     * Get all savings goals for the authenticated user
     */
    @GetMapping
    public ResponseEntity<Map<String, List<SavingsGoal>>> getAllGoals(Authentication auth) {
        User user = userService.findByUsername(auth.getName());
        List<SavingsGoal> goals = savingsGoalService.getAllGoals(user);
        
        Map<String, List<SavingsGoal>> response = new HashMap<>();
        response.put("goals", goals);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get a specific savings goal
     */
    @GetMapping("/{id}")
    public ResponseEntity<SavingsGoal> getGoal(@PathVariable Long id, Authentication auth) {
        User user = userService.findByUsername(auth.getName());
        SavingsGoal goal = savingsGoalService.getGoal(id, user);
        return ResponseEntity.ok(goal);
    }
    
    /**
     * Update a savings goal
     */
    @PutMapping("/{id}")
    public ResponseEntity<SavingsGoal> updateGoal(@PathVariable Long id, 
                                                  @Valid @RequestBody SavingsGoalUpdateRequest request, 
                                                  Authentication auth) {
        User user = userService.findByUsername(auth.getName());
        SavingsGoal goal = savingsGoalService.updateGoal(id, request, user);
        return ResponseEntity.ok(goal);
    }
    
    /**
     * Delete a savings goal
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteGoal(@PathVariable Long id, Authentication auth) {
        User user = userService.findByUsername(auth.getName());
        savingsGoalService.deleteGoal(id, user);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Goal deleted successfully");
        
        return ResponseEntity.ok(response);
    }
}
