package com.example.personalfinance.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

/**
 * SavingsGoal entity for tracking financial goals
 */
@Entity
@Table(name = "savings_goals")
public class SavingsGoal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Goal name is required")
    private String goalName;

    @Column(nullable = false, precision = 15, scale = 2)
    @DecimalMin(value = "0.01", message = "Target amount must be positive")
    @NotNull(message = "Target amount is required")
    private BigDecimal targetAmount;

    @Column(nullable = false)
    @NotNull(message = "Target date is required")
    @Future(message = "Target date must be in the future")
    private LocalDate targetDate;

    @Column(nullable = false)
    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"transactions", "customCategories", "savingsGoals", "password"})
    private User user;

    // Calculated fields
    @Transient
    private BigDecimal currentProgress = BigDecimal.ZERO;

    @Transient
    private Double progressPercentage = 0.0;

    @Transient
    private BigDecimal remainingAmount = BigDecimal.ZERO;

    public SavingsGoal() {
        this.startDate = LocalDate.now();
    }

    /**
     * Calculate progress based on income and expenses
     */
    public void calculateProgress(BigDecimal totalIncome, BigDecimal totalExpenses) {
        this.currentProgress = totalIncome.subtract(totalExpenses);
        if (this.currentProgress.compareTo(BigDecimal.ZERO) < 0) {
            this.currentProgress = BigDecimal.ZERO;
        }

        if (this.targetAmount.compareTo(BigDecimal.ZERO) > 0) {
            this.progressPercentage = this.currentProgress
                .divide(this.targetAmount, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .doubleValue();
        }

        this.remainingAmount = this.targetAmount.subtract(this.currentProgress);
        if (this.remainingAmount.compareTo(BigDecimal.ZERO) < 0) {
            this.remainingAmount = BigDecimal.ZERO;
        }
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getGoalName() { return goalName; }
    public void setGoalName(String goalName) { this.goalName = goalName; }

    public BigDecimal getTargetAmount() { return targetAmount; }
    public void setTargetAmount(BigDecimal targetAmount) { this.targetAmount = targetAmount; }

    public LocalDate getTargetDate() { return targetDate; }
    public void setTargetDate(LocalDate targetDate) { this.targetDate = targetDate; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public BigDecimal getCurrentProgress() { return currentProgress; }
    public void setCurrentProgress(BigDecimal currentProgress) { this.currentProgress = currentProgress; }

    public Double getProgressPercentage() { return progressPercentage; }
    public void setProgressPercentage(Double progressPercentage) { this.progressPercentage = progressPercentage; }

    public BigDecimal getRemainingAmount() { return remainingAmount; }
    public void setRemainingAmount(BigDecimal remainingAmount) { this.remainingAmount = remainingAmount; }
}
