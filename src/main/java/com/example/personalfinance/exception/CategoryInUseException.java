package com.example.personalfinance.exception;

/**
 * Exception thrown when attempting to delete a category that is in use
 */
public class CategoryInUseException extends RuntimeException {
    public CategoryInUseException(String message) {
        super(message);
    }
}
