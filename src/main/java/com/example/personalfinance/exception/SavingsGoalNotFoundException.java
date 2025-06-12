package com.example.personalfinance.exception;

/**
 * Exception thrown when a savings goal is not found
 */
public class SavingsGoalNotFoundException extends RuntimeException {
    public SavingsGoalNotFoundException(String message) {
        super(message);
    }
}
