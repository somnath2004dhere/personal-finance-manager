package com.example.personalfinance.service;

import com.example.personalfinance.dto.SavingsGoalRequest;
import com.example.personalfinance.dto.SavingsGoalUpdateRequest;
import com.example.personalfinance.entity.Category;
import com.example.personalfinance.entity.SavingsGoal;
import com.example.personalfinance.entity.User;
import com.example.personalfinance.exception.SavingsGoalNotFoundException;
import com.example.personalfinance.repository.SavingsGoalRepository;
import com.example.personalfinance.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Service class for savings goal management operations
 */
@Service
@Transactional
public class SavingsGoalService {
    
    @Autowired
    private SavingsGoalRepository savingsGoalRepository;
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    /**
     * Create a new savings goal
     */
    public SavingsGoal createGoal(SavingsGoalRequest request, User user) {
        SavingsGoal goal = new SavingsGoal();
        goal.setGoalName(request.getGoalName());
        goal.setTargetAmount(request.getTargetAmount());
        goal.setTargetDate(request.getTargetDate());
        
        if (request.getStartDate() != null) {
            goal.setStartDate(request.getStartDate());
        }
        
        goal.setUser(user);
        
        SavingsGoal savedGoal = savingsGoalRepository.save(goal);
        calculateProgress(savedGoal);
        return savedGoal;
    }
    
    /**
     * Get all goals for a user
     */
    public List<SavingsGoal> getAllGoals(User user) {
        List<SavingsGoal> goals = savingsGoalRepository.findByUser(user);
        goals.forEach(this::calculateProgress);
        return goals;
    }
    
    /**
     * Get a specific goal
     */
    public SavingsGoal getGoal(Long id, User user) {
        SavingsGoal goal = savingsGoalRepository.findByIdAndUser(id, user)
            .orElseThrow(() -> new SavingsGoalNotFoundException("Savings goal not found"));
        calculateProgress(goal);
        return goal;
    }
    
    /**
     * Update a savings goal
     */
    public SavingsGoal updateGoal(Long id, SavingsGoalUpdateRequest request, User user) {
        SavingsGoal goal = savingsGoalRepository.findByIdAndUser(id, user)
            .orElseThrow(() -> new SavingsGoalNotFoundException("Savings goal not found"));
        
        if (request.getTargetAmount() != null) {
            goal.setTargetAmount(request.getTargetAmount());
        }
        if (request.getTargetDate() != null) {
            goal.setTargetDate(request.getTargetDate());
        }
        
        SavingsGoal savedGoal = savingsGoalRepository.save(goal);
        calculateProgress(savedGoal);
        return savedGoal;
    }
    
    /**
     * Delete a savings goal
     */
    public void deleteGoal(Long id, User user) {
        SavingsGoal goal = savingsGoalRepository.findByIdAndUser(id, user)
            .orElseThrow(() -> new SavingsGoalNotFoundException("Savings goal not found"));
        
        savingsGoalRepository.delete(goal);
    }
    
    /**
     * Calculate progress for a savings goal
     */
    private void calculateProgress(SavingsGoal goal) {
        BigDecimal totalIncome = transactionRepository
            .sumAmountByUserAndTypeAndDateAfter(goal.getUser(), Category.CategoryType.INCOME, goal.getStartDate());
        BigDecimal totalExpenses = transactionRepository
            .sumAmountByUserAndTypeAndDateAfter(goal.getUser(), Category.CategoryType.EXPENSE, goal.getStartDate());
        
        if (totalIncome == null) totalIncome = BigDecimal.ZERO;
        if (totalExpenses == null) totalExpenses = BigDecimal.ZERO;
        
        goal.calculateProgress(totalIncome, totalExpenses);
    }
}
