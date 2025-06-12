package com.example.personalfinance.repository;

import com.example.personalfinance.entity.Category;
import com.example.personalfinance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Category entity
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByUserOrUserIsNull(User user);
    
    @Query("SELECT c FROM Category c WHERE (c.user = :user OR c.user IS NULL) AND c.name = :name")
    Optional<Category> findByNameAndUser(@Param("name") String name, @Param("user") User user);
    
    boolean existsByNameAndUser(String name, User user);
    
    List<Category> findByUserAndIsCustomTrue(User user);
    
    @Query("SELECT COUNT(t) > 0 FROM Transaction t WHERE t.category = :categoryName AND t.user = :user")
    boolean isCategoryUsedInTransactions(@Param("categoryName") String categoryName, @Param("user") User user);
}
