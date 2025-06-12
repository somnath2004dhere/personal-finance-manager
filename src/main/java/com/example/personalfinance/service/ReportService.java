package com.example.personalfinance.service;

import com.example.personalfinance.entity.Category;
import com.example.personalfinance.entity.User;
import com.example.personalfinance.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service class for generating financial reports
 */
@Service
public class ReportService {
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    /**
     * Generate monthly financial report
     */
    public Map<String, Object> getMonthlyReport(User user, int year, int month) {
        Map<String, Object> report = new HashMap<>();
        
        Map<String, BigDecimal> totalIncome = new HashMap<>();
        Map<String, BigDecimal> totalExpenses = new HashMap<>();
        
        List<Object[]> incomeData = transactionRepository
            .sumAmountByCategoryAndUserAndTypeAndYearAndMonth(user, Category.CategoryType.INCOME, year, month);
        List<Object[]> expenseData = transactionRepository
            .sumAmountByCategoryAndUserAndTypeAndYearAndMonth(user, Category.CategoryType.EXPENSE, year, month);
        
        BigDecimal totalIncomeAmount = BigDecimal.ZERO;
        for (Object[] data : incomeData) {
            String category = (String) data[0];
            BigDecimal amount = (BigDecimal) data[1];
            totalIncome.put(category, amount);
            totalIncomeAmount = totalIncomeAmount.add(amount);
        }
        
        BigDecimal totalExpenseAmount = BigDecimal.ZERO;
        for (Object[] data : expenseData) {
            String category = (String) data[0];
            BigDecimal amount = (BigDecimal) data[1];
            totalExpenses.put(category, amount);
            totalExpenseAmount = totalExpenseAmount.add(amount);
        }
        
        BigDecimal netSavings = totalIncomeAmount.subtract(totalExpenseAmount);
        
        report.put("month", month);
        report.put("year", year);
        report.put("totalIncome", totalIncome);
        report.put("totalExpenses", totalExpenses);
        report.put("netSavings", netSavings);
        
        return report;
    }
    
    /**
     * Generate yearly financial report
     */
    public Map<String, Object> getYearlyReport(User user, int year) {
        Map<String, Object> report = new HashMap<>();
        
        Map<String, BigDecimal> totalIncome = new HashMap<>();
        Map<String, BigDecimal> totalExpenses = new HashMap<>();
        
        List<Object[]> incomeData = transactionRepository
            .sumAmountByCategoryAndUserAndTypeAndYear(user, Category.CategoryType.INCOME, year);
        List<Object[]> expenseData = transactionRepository
            .sumAmountByCategoryAndUserAndTypeAndYear(user, Category.CategoryType.EXPENSE, year);
        
        BigDecimal totalIncomeAmount = BigDecimal.ZERO;
        for (Object[] data : incomeData) {
            String category = (String) data[0];
            BigDecimal amount = (BigDecimal) data[1];
            totalIncome.put(category, amount);
            totalIncomeAmount = totalIncomeAmount.add(amount);
        }
        
        BigDecimal totalExpenseAmount = BigDecimal.ZERO;
        for (Object[] data : expenseData) {
            String category = (String) data[0];
            BigDecimal amount = (BigDecimal) data[1];
            totalExpenses.put(category, amount);
            totalExpenseAmount = totalExpenseAmount.add(amount);
        }
        
        BigDecimal netSavings = totalIncomeAmount.subtract(totalExpenseAmount);
        
        report.put("year", year);
        report.put("totalIncome", totalIncome);
        report.put("totalExpenses", totalExpenses);
        report.put("netSavings", netSavings);
        
        return report;
    }
}
