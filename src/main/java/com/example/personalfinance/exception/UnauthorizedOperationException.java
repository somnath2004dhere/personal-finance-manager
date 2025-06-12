package com.example.personalfinance.exception;

/**
 * Exception thrown when an unauthorized operation is attempted
 */
public class UnauthorizedOperationException extends RuntimeException {
    public UnauthorizedOperationException(String message) {
        super(message);
    }
}
