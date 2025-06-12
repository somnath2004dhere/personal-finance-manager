package com.example.personalfinance.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Transaction entity representing financial transactions
 */
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 15, scale = 2)
    @DecimalMin(value = "0.01", message = "Amount must be positive")
    @NotNull(message = "Amount is required")
    private BigDecimal amount;

    @Column(nullable = false)
    @NotNull(message = "Date is required")
    @PastOrPresent(message = "Date cannot be in the future")
    private LocalDate date;

    @Column(nullable = false)
    @NotBlank(message = "Category is required")
    private String category;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category.CategoryType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"transactions", "customCategories", "savingsGoals", "password"})
    private User user;

    public Transaction() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Category.CategoryType getType() { return type; }
    public void setType(Category.CategoryType type) { this.type = type; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
