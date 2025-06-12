package com.example.personalfinance.service;

import com.example.personalfinance.dto.CategoryRequest;
import com.example.personalfinance.entity.Category;
import com.example.personalfinance.entity.User;
import com.example.personalfinance.exception.CategoryAlreadyExistsException;
import com.example.personalfinance.exception.CategoryInUseException;
import com.example.personalfinance.exception.CategoryNotFoundException;
import com.example.personalfinance.exception.UnauthorizedOperationException;
import com.example.personalfinance.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import java.util.List;

/**
 * Service class for category management operations
 */
@Service
@Transactional
public class CategoryService {
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    /**
     * Initialize default categories on application startup
     */
    @PostConstruct
    public void initializeDefaultCategories() {
        if (categoryRepository.count() == 0) {
            // Default INCOME categories
            categoryRepository.save(new Category("Salary", Category.CategoryType.INCOME, false, null));
            
            // Default EXPENSE categories
            categoryRepository.save(new Category("Food", Category.CategoryType.EXPENSE, false, null));
            categoryRepository.save(new Category("Rent", Category.CategoryType.EXPENSE, false, null));
            categoryRepository.save(new Category("Transportation", Category.CategoryType.EXPENSE, false, null));
            categoryRepository.save(new Category("Entertainment", Category.CategoryType.EXPENSE, false, null));
            categoryRepository.save(new Category("Healthcare", Category.CategoryType.EXPENSE, false, null));
            categoryRepository.save(new Category("Utilities", Category.CategoryType.EXPENSE, false, null));
        }
    }
    
    /**
     * Get all categories available to a user
     */
    public List<Category> getAllCategoriesForUser(User user) {
        return categoryRepository.findByUserOrUserIsNull(user);
    }
    
    /**
     * Create a custom category for a user
     */
    public Category createCustomCategory(CategoryRequest request, User user) {
        if (categoryRepository.existsByNameAndUser(request.getName(), user) || 
            categoryRepository.findByNameAndUser(request.getName(), null).isPresent()) {
            throw new CategoryAlreadyExistsException("Category with name " + request.getName() + " already exists");
        }
        
        Category category = new Category();
        category.setName(request.getName());
        category.setType(request.getType());
        category.setCustom(true);
        category.setUser(user);
        
        return categoryRepository.save(category);
    }
    
    /**
     * Delete a custom category
     */
    public void deleteCustomCategory(String categoryName, User user) {
        Category category = categoryRepository.findByNameAndUser(categoryName, user)
            .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
        
        if (!category.isCustom()) {
            throw new UnauthorizedOperationException("Cannot delete default categories");
        }
        
        if (categoryRepository.isCategoryUsedInTransactions(categoryName, user)) {
            throw new CategoryInUseException("Cannot delete category that is referenced by transactions");
        }
        
        categoryRepository.delete(category);
    }
    
    /**
     * Find category by name for a user
     */
    public Category findCategoryByNameForUser(String categoryName, User user) {
        return categoryRepository.findByNameAndUser(categoryName, user)
            .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
    }
}
