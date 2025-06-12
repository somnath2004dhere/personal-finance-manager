package com.example.personalfinance.controller;

import com.example.personalfinance.entity.User;
import com.example.personalfinance.service.ReportService;
import com.example.personalfinance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller for report operations
 */
@RestController
@RequestMapping("/reports")
public class ReportController {
    
    @Autowired
    private ReportService reportService;
    
    @Autowired
    private UserService userService;
    
    /**
     * Get monthly financial report
     */
    @GetMapping("/monthly/{year}/{month}")
    public ResponseEntity<Map<String, Object>> getMonthlyReport(@PathVariable int year, 
                                                               @PathVariable int month, 
                                                               Authentication auth) {
        User user = userService.findByUsername(auth.getName());
        Map<String, Object> report = reportService.getMonthlyReport(user, year, month);
        return ResponseEntity.ok(report);
    }
    
    /**
     * Get yearly financial report
     */
    @GetMapping("/yearly/{year}")
    public ResponseEntity<Map<String, Object>> getYearlyReport(@PathVariable int year, Authentication auth) {
        User user = userService.findByUsername(auth.getName());
        Map<String, Object> report = reportService.getYearlyReport(user, year);
        return ResponseEntity.ok(report);
    }
}
