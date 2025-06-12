package com.example.personalfinance.controller;

import com.example.personalfinance.dto.TransactionRequest;
import com.example.personalfinance.dto.TransactionUpdateRequest;
import com.example.personalfinance.entity.Transaction;
import com.example.personalfinance.entity.User;
import com.example.personalfinance.service.TransactionService;
import com.example.personalfinance.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for transaction operations
 */
@RestController
@RequestMapping("/transactions")
public class TransactionController {
    
    @Autowired
    private TransactionService transactionService;
    
    @Autowired
    private UserService userService;
    
    /**
     * Create a new transaction
     */
    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@Valid @RequestBody TransactionRequest request, Authentication auth) {
        User user = userService.findByUsername(auth.getName());
        Transaction transaction = transactionService.createTransaction(request, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
    }
    
    /**
     * Get transactions with optional filtering
     */
    @GetMapping
    public ResponseEntity<Map<String, List<Transaction>>> getTransactions(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String category,
            Authentication auth) {
        
        User user = userService.findByUsername(auth.getName());
        List<Transaction> transactions;
        
        if (startDate != null && endDate != null) {
            transactions = transactionService.getTransactionsByDateRange(user, startDate, endDate);
        } else if (category != null && !category.isEmpty()) {
            transactions = transactionService.getTransactionsByCategory(user, category);
        } else {
            transactions = transactionService.getAllTransactions(user);
        }
        
        Map<String, List<Transaction>> response = new HashMap<>();
        response.put("transactions", transactions);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Update a transaction
     */
    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable Long id, 
                                                        @Valid @RequestBody TransactionUpdateRequest request, 
                                                        Authentication auth) {
        User user = userService.findByUsername(auth.getName());
        Transaction transaction = transactionService.updateTransaction(id, request, user);
        return ResponseEntity.ok(transaction);
    }
    
    /**
     * Delete a transaction
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteTransaction(@PathVariable Long id, Authentication auth) {
        User user = userService.findByUsername(auth.getName());
        transactionService.deleteTransaction(id, user);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Transaction deleted successfully");
        
//        return ResponseEntity.  "Transaction deleted successfully");
        
        return ResponseEntity.ok(response);
    }
}
