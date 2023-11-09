package com.example.wanderlust.service;

import com.example.wanderlust.model.Expense;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface ExpenseService {

    Optional<Expense> getExpenseById(UUID uuid, Optional<String> matchUsername);

    Page<Expense> getExpensesByUsername(String username, int page, int limit);

    Page<Expense> getExpensesByAmount(BigDecimal low, BigDecimal high, Optional<String> matchUsername, int page, int limit);

    Page<Expense> getExpensesByCategory(String category, Optional<String> matchUsername,int page, int limit);

    Expense createExpense(UUID itineraryId, Expense expense, Optional<String> matchUsername);

    Expense editExpense(UUID uuid, Expense expense, Optional<String> matchUsername);

    Expense deleteExpense(UUID uuid, Optional<String> matchUsername);
}
