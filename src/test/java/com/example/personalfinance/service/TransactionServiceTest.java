package com.example.personalfinance.service;

import com.example.personalfinance.dto.TransactionRequest;
import com.example.personalfinance.dto.TransactionUpdateRequest;
import com.example.personalfinance.entity.Category;
import com.example.personalfinance.entity.Transaction;
import com.example.personalfinance.entity.User;
import com.example.personalfinance.exception.TransactionNotFoundException;
import com.example.personalfinance.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Unit tests for TransactionService
 */
@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private TransactionService transactionService;

    private User user;
    private Category category;
    private TransactionRequest transactionRequest;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("test@example.com");

        category = new Category();
        category.setName("Salary");
        category.setType(Category.CategoryType.INCOME);

        transactionRequest = new TransactionRequest();
        transactionRequest.setAmount(new BigDecimal("1000.00"));
        transactionRequest.setDate(LocalDate.now());
        transactionRequest.setCategory("Salary");
        transactionRequest.setDescription("Monthly salary");

        transaction = new Transaction();
        transaction.setId(1L);
        transaction.setAmount(new BigDecimal("1000.00"));
        transaction.setDate(LocalDate.now());
        transaction.setCategory("Salary");
        transaction.setDescription("Monthly salary");
        transaction.setType(Category.CategoryType.INCOME);
        transaction.setUser(user);
    }

    @Test
    void createTransaction_Success() {
        // Given
        when(categoryService.findCategoryByNameForUser("Salary", user)).thenReturn(category);
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> {
            Transaction savedTransaction = invocation.getArgument(0);
            savedTransaction.setId(1L);
            return savedTransaction;
        });

        // When
        Transaction result = transactionService.createTransaction(transactionRequest, user);

        // Then
        assertNotNull(result);
        assertEquals(new BigDecimal("1000.00"), result.getAmount());
        assertEquals("Salary", result.getCategory());
        assertEquals(Category.CategoryType.INCOME, result.getType());
        assertEquals(user, result.getUser());

        verify(categoryService).findCategoryByNameForUser("Salary", user);
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void getAllTransactions_Success() {
        // Given
        List<Transaction> transactions = Arrays.asList(transaction);
        when(transactionRepository.findByUserOrderByDateDesc(user)).thenReturn(transactions);

        // When
        List<Transaction> result = transactionService.getAllTransactions(user);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(transaction, result.get(0));

        verify(transactionRepository).findByUserOrderByDateDesc(user);
    }

    @Test
    void getTransactionsByDateRange_Success() {
        // Given
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 31);
        List<Transaction> transactions = Arrays.asList(transaction);
        when(transactionRepository.findByUserAndDateBetweenOrderByDateDesc(user, startDate, endDate))
            .thenReturn(transactions);

        // When
        List<Transaction> result = transactionService.getTransactionsByDateRange(user, startDate, endDate);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(transaction, result.get(0));

        verify(transactionRepository).findByUserAndDateBetweenOrderByDateDesc(user, startDate, endDate);
    }

    @Test
    void getTransactionsByCategory_Success() {
        // Given
        List<Transaction> transactions = Arrays.asList(transaction);
        when(transactionRepository.findByUserAndCategoryOrderByDateDesc(user, "Salary"))
            .thenReturn(transactions);

        // When
        List<Transaction> result = transactionService.getTransactionsByCategory(user, "Salary");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(transaction, result.get(0));

        verify(transactionRepository).findByUserAndCategoryOrderByDateDesc(user, "Salary");
    }

    @Test
    void updateTransaction_Success() {
        // Given
        TransactionUpdateRequest updateRequest = new TransactionUpdateRequest();
        updateRequest.setAmount(new BigDecimal("1500.00"));
        updateRequest.setDescription("Updated salary");

        when(transactionRepository.findById(anyLong())).thenReturn(Optional.of(transaction));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        // When
        Transaction result = transactionService.updateTransaction(1L, updateRequest, user);

        // Then
        assertNotNull(result);
        verify(transactionRepository).findById(1L);
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void updateTransaction_NotFound() {
        // Given
        TransactionUpdateRequest updateRequest = new TransactionUpdateRequest();
        when(transactionRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        TransactionNotFoundException exception = assertThrows(TransactionNotFoundException.class, () -> {
            transactionService.updateTransaction(1L, updateRequest, user);
        });

        assertEquals("Transaction not found", exception.getMessage());
        verify(transactionRepository).findById(1L);
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void updateTransaction_UnauthorizedUser() {
        // Given
        User otherUser = new User();
        otherUser.setId(2L);
        otherUser.setUsername("other@example.com");

        TransactionUpdateRequest updateRequest = new TransactionUpdateRequest();
        updateRequest.setAmount(new BigDecimal("1500.00"));

        when(transactionRepository.findById(anyLong())).thenReturn(Optional.of(transaction));

        // When & Then
        TransactionNotFoundException exception = assertThrows(TransactionNotFoundException.class, () -> {
            transactionService.updateTransaction(1L, updateRequest, otherUser);
        });

        assertEquals("Transaction not found", exception.getMessage());
        verify(transactionRepository).findById(1L);
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void deleteTransaction_Success() {
        // Given
        when(transactionRepository.findById(anyLong())).thenReturn(Optional.of(transaction));

        // When
        transactionService.deleteTransaction(1L, user);

        // Then
        verify(transactionRepository).findById(1L);
        verify(transactionRepository).delete(transaction);
    }

    @Test
    void deleteTransaction_NotFound() {
        // Given
        when(transactionRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        TransactionNotFoundException exception = assertThrows(TransactionNotFoundException.class, () -> {
            transactionService.deleteTransaction(1L, user);
        });

        assertEquals("Transaction not found", exception.getMessage());
        verify(transactionRepository).findById(1L);
        verify(transactionRepository, never()).delete(any(Transaction.class));
    }

    @Test
    void deleteTransaction_UnauthorizedUser() {
        // Given
        User otherUser = new User();
        otherUser.setId(2L);
        otherUser.setUsername("other@example.com");

        when(transactionRepository.findById(anyLong())).thenReturn(Optional.of(transaction));

        // When & Then
        TransactionNotFoundException exception = assertThrows(TransactionNotFoundException.class, () -> {
            transactionService.deleteTransaction(1L, otherUser);
        });

        assertEquals("Transaction not found", exception.getMessage());
        verify(transactionRepository).findById(1L);
        verify(transactionRepository, never()).delete(any(Transaction.class));
    }
}
