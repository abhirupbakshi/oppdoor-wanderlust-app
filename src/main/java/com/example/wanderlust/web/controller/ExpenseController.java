package com.example.wanderlust.web.controller;

import com.example.wanderlust.exception.ExpenseAbsentException;
import com.example.wanderlust.exception.HttpMethodNotImplementedException;
import com.example.wanderlust.exception.IllegalRequestException;
import com.example.wanderlust.model.Expense;
import com.example.wanderlust.model.validation.group.Create;
import com.example.wanderlust.service.ExpenseService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    @Autowired
    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping("{uuid}")
    ResponseEntity<Expense> getExpenseById(@PathVariable("uuid") UUID uuid, Authentication authentication) {

        String username = authentication.getName();
        Optional<Expense> optionalExpense = expenseService.getExpenseById(uuid, Optional.of(username));

        if (optionalExpense.isEmpty()) {
            throw new ExpenseAbsentException("Expense with uuid: " + uuid + " does not exist");
        }

        return ResponseEntity.ok(optionalExpense.get());
    }

    @GetMapping
    ResponseEntity<List<Expense>> getExpenses(
            @RequestParam(value = "low", required = false) BigDecimal low,
            @RequestParam(value = "high", required = false) BigDecimal high,
            @RequestParam(value = "page", defaultValue = "1", required = false) int page,
            @RequestParam(value = "limit", defaultValue = "10", required = false) int limit,
            Authentication authentication) {

        if (page < 1) {
            throw new IllegalRequestException("Page number must be greater than or equal to 1");
        }
        if (limit < 1) {
            throw new IllegalRequestException("Page limit must be greater than or equal to 1");
        }

        String username = authentication.getName();
        Page<Expense> expensePage;

        if (low != null && high != null) {
            if (low.compareTo(high) > 0) {
                throw new IllegalRequestException("The lower expense amount limit should be lesser or equal to than higher limit");
            } else {
                expensePage = expenseService.getExpensesByAmount(low, high, Optional.of(username), page, limit);
            }
        } else {
            expensePage = expenseService.getExpensesByUsername(username, page, limit);
        }

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(expensePage.getTotalElements()))
                .body(expensePage.getContent());
    }

    @GetMapping("category/{category}")
    ResponseEntity<List<Expense>> getExpensesByCategory(
            @PathVariable("category") String category,
            @RequestParam(value = "page", defaultValue = "1", required = false) int page,
            @RequestParam(value = "limit", defaultValue = "10", required = false) int limit,
            Authentication authentication) {

        if (page < 1) {
            throw new IllegalRequestException("Page number must be greater than or equal to 1");
        }
        if (limit < 1) {
            throw new IllegalRequestException("Page limit must be greater than or equal to 1");
        }

        String username = authentication.getName();
        Page<Expense> expensePage = expenseService.getExpensesByCategory(category, Optional.of(username), page, limit);

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(expensePage.getTotalElements()))
                .body(expensePage.getContent());
    }

    @PostMapping("{itineraryId}")
    ResponseEntity<Expense> createExpense(
            @PathVariable("itineraryId") UUID itineraryId,
            @Validated(Create.class) @RequestBody Expense expense,
            Authentication authentication,
            HttpServletRequest request) {

        String username = authentication.getName();
        expense = expenseService.createExpense(itineraryId, expense, Optional.of(username));

        return ResponseEntity.created(URI.create(request.getRequestURI())).body(expense);
    }

    @PutMapping
    ResponseEntity<Expense> editExpense() {

        throw new HttpMethodNotImplementedException("Update itinerary method has net been implemented yet");
    }

    @DeleteMapping("{uuid}")
    ResponseEntity<Expense> deleteExpense(@PathVariable("uuid") UUID uuid, Authentication authentication) {

        String username = authentication.getName();
        Expense expense = expenseService.deleteExpense(uuid, Optional.of(username));

        return ResponseEntity.ok(expense);
    }
}
