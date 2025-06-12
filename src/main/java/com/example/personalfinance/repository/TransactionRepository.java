package com.example.personalfinance.repository;

import com.example.personalfinance.entity.Category;
import com.example.personalfinance.entity.Transaction;
import com.example.personalfinance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for Transaction entity
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserOrderByDateDesc(User user);
    
    List<Transaction> findByUserAndDateBetweenOrderByDateDesc(User user, LocalDate startDate, LocalDate endDate);
    
    List<Transaction> findByUserAndCategoryOrderByDateDesc(User user, String category);
    
    List<Transaction> findByUserAndTypeOrderByDateDesc(User user, Category.CategoryType type);
    
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.user = :user AND t.type = :type AND t.date >= :startDate")
    BigDecimal sumAmountByUserAndTypeAndDateAfter(@Param("user") User user, 
                                                  @Param("type") Category.CategoryType type, 
                                                  @Param("startDate") LocalDate startDate);
    
    @Query("SELECT t.category, COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.user = :user AND t.type = :type AND YEAR(t.date) = :year AND MONTH(t.date) = :month GROUP BY t.category")
    List<Object[]> sumAmountByCategoryAndUserAndTypeAndYearAndMonth(@Param("user") User user, 
                                                                    @Param("type") Category.CategoryType type, 
                                                                    @Param("year") int year, 
                                                                    @Param("month") int month);
    
    @Query("SELECT t.category, COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.user = :user AND t.type = :type AND YEAR(t.date) = :year GROUP BY t.category")
    List<Object[]> sumAmountByCategoryAndUserAndTypeAndYear(@Param("user") User user, 
                                                            @Param("type") Category.CategoryType type, 
                                                            @Param("year") int year);
}
