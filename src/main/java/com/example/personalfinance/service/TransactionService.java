package com.example.personalfinance.service;

import com.example.personalfinance.dto.TransactionRequest;
import com.example.personalfinance.dto.TransactionUpdateRequest;
import com.example.personalfinance.entity.Category;
import com.example.personalfinance.entity.Transaction;
import com.example.personalfinance.entity.User;
import com.example.personalfinance.exception.TransactionNotFoundException;
import com.example.personalfinance.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Service class for transaction management operations
 */
@Service
@Transactional
public class TransactionService {
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private CategoryService categoryService;
    
    /**
     * Create a new transaction
     */
    public Transaction createTransaction(TransactionRequest request, User user) {
        Category category = categoryService.findCategoryByNameForUser(request.getCategory(), user);
        
        Transaction transaction = new Transaction();
        transaction.setAmount(request.getAmount());
        transaction.setDate(request.getDate());
        transaction.setCategory(request.getCategory());
        transaction.setDescription(request.getDescription());
        transaction.setType(category.getType());
        transaction.setUser(user);
        
        return transactionRepository.save(transaction);
    }
    
    /**
     * Get all transactions for a user
     */
    public List<Transaction> getAllTransactions(User user) {
        return transactionRepository.findByUserOrderByDateDesc(user);
    }
    
    /**
     * Get transactions by date range
     */
    public List<Transaction> getTransactionsByDateRange(User user, LocalDate startDate, LocalDate endDate) {
        return transactionRepository.findByUserAndDateBetweenOrderByDateDesc(user, startDate, endDate);
    }
    
    /**
     * Get transactions by category
     */
    public List<Transaction> getTransactionsByCategory(User user, String category) {
        return transactionRepository.findByUserAndCategoryOrderByDateDesc(user, category);
    }
    
    /**
     * Update a transaction
     */
    public Transaction updateTransaction(Long id, TransactionUpdateRequest request, User user) {
        Transaction transaction = transactionRepository.findById(id)
            .orElseThrow(() -> new TransactionNotFoundException("Transaction not found"));
        
        if (!transaction.getUser().getId().equals(user.getId())) {
            throw new TransactionNotFoundException("Transaction not found");
        }
        
        if (request.getAmount() != null) {
            transaction.setAmount(request.getAmount());
        }
        if (request.getDescription() != null) {
            transaction.setDescription(request.getDescription());
        }
        
        return transactionRepository.save(transaction);
    }
    
    /**
     * Delete a transaction
     */
    public void deleteTransaction(Long id, User user) {
        Transaction transaction = transactionRepository.findById(id)
            .orElseThrow(() -> new TransactionNotFoundException("Transaction not found"));
        
        if (!transaction.getUser().getId().equals(user.getId())) {
            throw new TransactionNotFoundException("Transaction not found");
        }
        
        transactionRepository.delete(transaction);
    }
}
