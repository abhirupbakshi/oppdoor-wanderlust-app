package com.example.wanderlust.repository;

import com.example.wanderlust.model.Expense;
import com.example.wanderlust.model.Itinerary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.UUID;

public interface ExpenseRepository extends JpaRepository<Expense, UUID> {

    @Query("""
            SELECT e
            FROM Expense e
            WHERE e.itinerary.destination.user.username = :username
            """)
    Page<Expense> findExpensesByUsername(@Param("username") String username, Pageable pageable);

    @Query("""
            SELECT e
            FROM Expense e
            WHERE LOWER(e.category) = LOWER(:category)
            """)
    Page<Expense> findExpensesByCategory(@Param("category") String category, Pageable pageable);

    @Query("""
            SELECT e
            FROM Expense e
            WHERE e.amount BETWEEN :low AND :high
            """)
    Page<Expense> findExpensesByAmount(@Param("low") BigDecimal low, @Param("high") BigDecimal high, Pageable pageable);
}
