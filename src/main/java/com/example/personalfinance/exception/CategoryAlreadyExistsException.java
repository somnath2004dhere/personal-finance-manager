package com.example.personalfinance.exception;

/**
 * Exception thrown when attempting to create a category that already exists
 */
public class CategoryAlreadyExistsException extends RuntimeException {
    public CategoryAlreadyExistsException(String message) {
        super(message);
    }
}
