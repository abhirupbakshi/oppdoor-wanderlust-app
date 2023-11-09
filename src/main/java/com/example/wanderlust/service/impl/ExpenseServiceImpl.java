package com.example.wanderlust.service.impl;

import com.example.wanderlust.exception.ExpenseAbsentException;
import com.example.wanderlust.exception.ItineraryAbsentException;
import com.example.wanderlust.model.Expense;
import com.example.wanderlust.model.Itinerary;
import com.example.wanderlust.repository.ExpenseRepository;
import com.example.wanderlust.repository.ItineraryRepository;
import com.example.wanderlust.repository.UserRepository;
import com.example.wanderlust.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
public class ExpenseServiceImpl extends AbstractUserDependentEntityServiceImpl<Expense, UUID> implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ItineraryRepository itineraryRepository;

    @Autowired
    public ExpenseServiceImpl(ExpenseRepository expenseRepository, UserRepository userRepository, ItineraryRepository itineraryRepository) {

        super(userRepository);
        this.expenseRepository = expenseRepository;
        this.itineraryRepository = itineraryRepository;
    }

    @Override
    protected String extractUsernameFromEntity(Expense expense) {

        return expense.getItinerary().getDestination().getUser().getUsername();
    }

    @Override
    public Optional<Expense> getExpenseById(UUID uuid, Optional<String> matchUsername) {

        Assert.notNull(uuid, "UUID cannot be null");
        Assert.notNull(matchUsername, "Optional username cannot be null");

        return super.getEntityById(uuid, matchUsername, expenseRepository);
    }

    @Override
    public Page<Expense> getExpensesByUsername(String username, int page, int limit) {

        Assert.notNull(username, "Username cannot be null");
        Assert.isTrue(page >= 1, "Page number must be greater than or equal to 1");
        Assert.isTrue(limit >= 1, "Page content limit must be greater than or equal to 1");

        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Expense> expenses = expenseRepository.findExpensesByUsername(username, pageable);

        return expenses;
    }

    @Override
    public Page<Expense> getExpensesByAmount(BigDecimal low, BigDecimal high, Optional<String> matchUsername, int page, int limit) {

        Assert.notNull(low, "Low amount value cannot be null");
        Assert.notNull(high, "High amount value cannot be null");
        Assert.notNull(matchUsername, "Optional username cannot be null");
        Assert.isTrue(page >= 1, "Page number must be greater than or equal to 1");
        Assert.isTrue(limit >= 1, "Page content limit must be greater than or equal to 1");
        Assert.isTrue(low.compareTo(high) <= 0, "The lower limit should be lesser or equal to than higher limit");

        Pageable pageable = PageRequest.of(page - 1, limit);
        return expenseRepository.findExpensesByAmount(low, high, pageable);
    }

    @Override
    public Page<Expense> getExpensesByCategory(String category, Optional<String> matchUsername, int page, int limit) {

        Assert.notNull(category, "Category cannot be null");
        Assert.notNull(matchUsername, "Optional username cannot be null");
        Assert.isTrue(page >= 1, "Page number must be greater than or equal to 1");
        Assert.isTrue(limit >= 1, "Page content limit must be greater than or equal to 1");

        Pageable pageable = PageRequest.of(page - 1, limit);
        return expenseRepository.findExpensesByCategory(category, pageable);
    }

    @Override
    public Expense createExpense(UUID itineraryId, Expense expense, Optional<String> matchUsername) {

        Assert.notNull(itineraryId, "itinerary uuid cannot be null");
        Assert.notNull(expense, "Expense cannot be null");
        Assert.notNull(matchUsername, "Optional username cannot be null");

        String itineraryAbsentErrMsg = "Itinerary with UUID: " + itineraryId + " does not exist";
        Itinerary fetchedItinerary = itineraryRepository.findById(itineraryId)
                .orElseThrow(() -> new ItineraryAbsentException(itineraryAbsentErrMsg));

        if (matchUsername.isPresent()) {

            String provided = matchUsername.get();
            String actual = fetchedItinerary.getDestination().getUser().getUsername();

            if (!provided.equals(actual)) {
                throw new ItineraryAbsentException(itineraryAbsentErrMsg);
            }
        }

        expense
                .setUuid(null)
                .setItinerary(fetchedItinerary);

        return expenseRepository.save(expense);
    }

    @Override
    public Expense editExpense(UUID uuid, Expense expense, Optional<String> matchUsername) {

        throw new RuntimeException("Method not implemented");
    }

    @Override
    public Expense deleteExpense(UUID uuid, Optional<String> matchUsername) {

        Assert.notNull(uuid, "UUID cannot be null");
        Assert.notNull(matchUsername, "Optional username cannot be null");

        String expenseAbsentErrMsg = "Expense with UUID: " + uuid + " does not exist";

        return super.deleteEntity(uuid, matchUsername, expenseRepository, () -> new ExpenseAbsentException(expenseAbsentErrMsg));
    }
}
