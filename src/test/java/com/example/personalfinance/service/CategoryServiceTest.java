package com.example.personalfinance.service;

import com.example.personalfinance.dto.CategoryRequest;
import com.example.personalfinance.entity.Category;
import com.example.personalfinance.entity.User;
import com.example.personalfinance.exception.CategoryAlreadyExistsException;
import com.example.personalfinance.exception.CategoryInUseException;
import com.example.personalfinance.exception.CategoryNotFoundException;
import com.example.personalfinance.exception.UnauthorizedOperationException;
import com.example.personalfinance.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for CategoryService
 */
@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private User user;
    private Category defaultCategory;
    private Category customCategory;
    private CategoryRequest categoryRequest;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("test@example.com");

        defaultCategory = new Category();
        defaultCategory.setId(1L);
        defaultCategory.setName("Food");
        defaultCategory.setType(Category.CategoryType.EXPENSE);
        defaultCategory.setCustom(false);
        defaultCategory.setUser(null);

        customCategory = new Category();
        customCategory.setId(2L);
        customCategory.setName("Groceries");
        customCategory.setType(Category.CategoryType.EXPENSE);
        customCategory.setCustom(true);
        customCategory.setUser(user);

        categoryRequest = new CategoryRequest();
        categoryRequest.setName("Groceries");
        categoryRequest.setType(Category.CategoryType.EXPENSE);
    }

    @Test
    void getAllCategoriesForUser_Success() {
        // Given
        List<Category> categories = Arrays.asList(defaultCategory, customCategory);
        when(categoryRepository.findByUserOrUserIsNull(user)).thenReturn(categories);

        // When
        List<Category> result = categoryService.getAllCategoriesForUser(user);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(defaultCategory));
        assertTrue(result.contains(customCategory));

        verify(categoryRepository).findByUserOrUserIsNull(user);
    }

    @Test
    void createCustomCategory_Success() {
        // Given
        when(categoryRepository.existsByNameAndUser(anyString(), any(User.class))).thenReturn(false);
        when(categoryRepository.findByNameAndUser(anyString(), isNull())).thenReturn(Optional.empty());
        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> {
            Category category = invocation.getArgument(0);
            category.setId(2L);
            return category;
        });

        // When
        Category result = categoryService.createCustomCategory(categoryRequest, user);

        // Then
        assertNotNull(result);
        assertEquals("Groceries", result.getName());
        assertEquals(Category.CategoryType.EXPENSE, result.getType());
        assertTrue(result.isCustom());
        assertEquals(user, result.getUser());

        verify(categoryRepository).existsByNameAndUser("Groceries", user);
        verify(categoryRepository).findByNameAndUser("Groceries", null);
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void createCustomCategory_AlreadyExists() {
        // Given
        when(categoryRepository.existsByNameAndUser(anyString(), any(User.class))).thenReturn(true);

        // When & Then
        CategoryAlreadyExistsException exception = assertThrows(CategoryAlreadyExistsException.class, () -> {
            categoryService.createCustomCategory(categoryRequest, user);
        });

        assertEquals("Category with name Groceries already exists", exception.getMessage());
        verify(categoryRepository).existsByNameAndUser("Groceries", user);
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void createCustomCategory_DefaultCategoryExists() {
        // Given
        when(categoryRepository.existsByNameAndUser(anyString(), any(User.class))).thenReturn(false);
        when(categoryRepository.findByNameAndUser(anyString(), isNull())).thenReturn(Optional.of(defaultCategory));

        // When & Then
        CategoryAlreadyExistsException exception = assertThrows(CategoryAlreadyExistsException.class, () -> {
            categoryService.createCustomCategory(categoryRequest, user);
        });

        assertEquals("Category with name Groceries already exists", exception.getMessage());
        verify(categoryRepository).existsByNameAndUser("Groceries", user);
        verify(categoryRepository).findByNameAndUser("Groceries", null);
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void deleteCustomCategory_Success() {
        // Given
        when(categoryRepository.findByNameAndUser(anyString(), any(User.class))).thenReturn(Optional.of(customCategory));
        when(categoryRepository.isCategoryUsedInTransactions(anyString(), any(User.class))).thenReturn(false);

        // When
        categoryService.deleteCustomCategory("Groceries", user);

        // Then
        verify(categoryRepository).findByNameAndUser("Groceries", user);
        verify(categoryRepository).isCategoryUsedInTransactions("Groceries", user);
        verify(categoryRepository).delete(customCategory);
    }

    @Test
    void deleteCustomCategory_NotFound() {
        // Given
        when(categoryRepository.findByNameAndUser(anyString(), any(User.class))).thenReturn(Optional.empty());

        // When & Then
        CategoryNotFoundException exception = assertThrows(CategoryNotFoundException.class, () -> {
            categoryService.deleteCustomCategory("Groceries", user);
        });

        assertEquals("Category not found", exception.getMessage());
        verify(categoryRepository).findByNameAndUser("Groceries", user);
        verify(categoryRepository, never()).delete(any(Category.class));
    }

    @Test
    void deleteCustomCategory_DefaultCategory() {
        // Given
        when(categoryRepository.findByNameAndUser(anyString(), any(User.class))).thenReturn(Optional.of(defaultCategory));

        // When & Then
        UnauthorizedOperationException exception = assertThrows(UnauthorizedOperationException.class, () -> {
            categoryService.deleteCustomCategory("Food", user);
        });

        assertEquals("Cannot delete default categories", exception.getMessage());
        verify(categoryRepository).findByNameAndUser("Food", user);
        verify(categoryRepository, never()).delete(any(Category.class));
    }

    @Test
    void deleteCustomCategory_InUse() {
        // Given
        when(categoryRepository.findByNameAndUser(anyString(), any(User.class))).thenReturn(Optional.of(customCategory));
        when(categoryRepository.isCategoryUsedInTransactions(anyString(), any(User.class))).thenReturn(true);

        // When & Then
        CategoryInUseException exception = assertThrows(CategoryInUseException.class, () -> {
            categoryService.deleteCustomCategory("Groceries", user);
        });

        assertEquals("Cannot delete category that is referenced by transactions", exception.getMessage());
        verify(categoryRepository).findByNameAndUser("Groceries", user);
        verify(categoryRepository).isCategoryUsedInTransactions("Groceries", user);
        verify(categoryRepository, never()).delete(any(Category.class));
    }

    @Test
    void findCategoryByNameForUser_Success() {
        // Given
        when(categoryRepository.findByNameAndUser(anyString(), any(User.class))).thenReturn(Optional.of(customCategory));

        // When
        Category result = categoryService.findCategoryByNameForUser("Groceries", user);

        // Then
        assertNotNull(result);
        assertEquals(customCategory, result);
        verify(categoryRepository).findByNameAndUser("Groceries", user);
    }

    @Test
    void findCategoryByNameForUser_NotFound() {
        // Given
        when(categoryRepository.findByNameAndUser(anyString(), any(User.class))).thenReturn(Optional.empty());

        // When & Then
        CategoryNotFoundException exception = assertThrows(CategoryNotFoundException.class, () -> {
            categoryService.findCategoryByNameForUser("Groceries", user);
        });

        assertEquals("Category not found", exception.getMessage());
        verify(categoryRepository).findByNameAndUser("Groceries", user);
    }
}
