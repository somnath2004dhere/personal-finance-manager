package com.example.personalfinance.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for savings goal creation requests
 */
public class SavingsGoalRequest {
    @NotBlank(message = "Goal name is required")
    private String goalName;

    @DecimalMin(value = "0.01", message = "Target amount must be positive")
    @NotNull(message = "Target amount is required")
    private BigDecimal targetAmount;

    @NotNull(message = "Target date is required")
    @Future(message = "Target date must be in the future")
    private LocalDate targetDate;

    private LocalDate startDate;

    // Getters and Setters
    public String getGoalName() { return goalName; }
    public void setGoalName(String goalName) { this.goalName = goalName; }

    public BigDecimal getTargetAmount() { return targetAmount; }
    public void setTargetAmount(BigDecimal targetAmount) { this.targetAmount = targetAmount; }

    public LocalDate getTargetDate() { return targetDate; }
    public void setTargetDate(LocalDate targetDate) { this.targetDate = targetDate; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
}
