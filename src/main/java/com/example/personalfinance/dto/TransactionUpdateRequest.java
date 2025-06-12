package com.example.personalfinance.dto;

import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;

/**
 * DTO for transaction update requests
 */
public class TransactionUpdateRequest {
    @DecimalMin(value = "0.01", message = "Amount must be positive")
    private BigDecimal amount;

    private String description;

    // Getters and Setters
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
