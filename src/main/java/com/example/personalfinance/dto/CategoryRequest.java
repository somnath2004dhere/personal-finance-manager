package com.example.personalfinance.dto;

import com.example.personalfinance.entity.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for category creation requests
 */
public class CategoryRequest {
    @NotBlank(message = "Category name is required")
    private String name;

    @NotNull(message = "Category type is required")
    private Category.CategoryType type;

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Category.CategoryType getType() { return type; }
    public void setType(Category.CategoryType type) { this.type = type; }
}
