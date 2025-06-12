package com.example.personalfinance.repository;

import com.example.personalfinance.entity.SavingsGoal;
import com.example.personalfinance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for SavingsGoal entity
 */
@Repository
public interface SavingsGoalRepository extends JpaRepository<SavingsGoal, Long> {
    List<SavingsGoal> findByUser(User user);
    Optional<SavingsGoal> findByIdAndUser(Long id, User user);
}
