package com.example.personalfinance.exception;

/**
 * Exception thrown when a transaction is not found
 */
public class TransactionNotFoundException extends RuntimeException {
    public TransactionNotFoundException(String message) {
        super(message);
    }
}
