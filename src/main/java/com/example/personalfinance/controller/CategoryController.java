package com.example.personalfinance.controller;

import com.example.personalfinance.dto.CategoryRequest;
import com.example.personalfinance.entity.Category;
import com.example.personalfinance.entity.User;
import com.example.personalfinance.service.CategoryService;
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
 * Controller for category operations
 */
@RestController
@RequestMapping("/categories")
public class CategoryController {
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private UserService userService;
    
    /**
     * Get all categories for the authenticated user
     */
    @GetMapping
    public ResponseEntity<Map<String, List<Category>>> getAllCategories(Authentication auth) {
        User user = userService.findByUsername(auth.getName());
        List<Category> categories = categoryService.getAllCategoriesForUser(user);
        
        Map<String, List<Category>> response = new HashMap<>();
        response.put("categories", categories);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Create a custom category
     */
    @PostMapping
    public ResponseEntity<Category> createCategory(@Valid @RequestBody CategoryRequest request, Authentication auth) {
        User user = userService.findByUsername(auth.getName());
        Category category = categoryService.createCustomCategory(request, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }
    
    /**
     * Delete a custom category
     */
    @DeleteMapping("/{name}")
    public ResponseEntity<Map<String, String>> deleteCategory(@PathVariable String name, Authentication auth) {
        User user = userService.findByUsername(auth.getName());
        categoryService.deleteCustomCategory(name, user);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Category deleted successfully");
        
        return ResponseEntity.ok(response);
    }
}
