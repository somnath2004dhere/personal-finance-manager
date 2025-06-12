package com.example.personalfinance.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for savings goal update requests
 */
public class SavingsGoalUpdateRequest {
    @DecimalMin(value = "0.01", message = "Target amount must be positive")
    private BigDecimal targetAmount;

    @Future(message = "Target date must be in the future")
    private LocalDate targetDate;

    // Getters and Setters
    public BigDecimal getTargetAmount() { return targetAmount; }
    public void setTargetAmount(BigDecimal targetAmount) { this.targetAmount = targetAmount; }

    public LocalDate getTargetDate() { return targetDate; }
    public void setTargetDate(LocalDate targetDate) { this.targetDate = targetDate; }
}
