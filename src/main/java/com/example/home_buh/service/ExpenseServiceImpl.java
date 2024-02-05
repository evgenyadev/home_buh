package com.example.home_buh.service;

import com.example.home_buh.model.Expense;
import com.example.home_buh.model.User;
import com.example.home_buh.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserService userService;

    @Autowired
    public ExpenseServiceImpl(ExpenseRepository expenseRepository, UserService userService) {
        this.expenseRepository = expenseRepository;
        this.userService = userService;
    }

    @Override
    public Expense createExpense(Expense expense) {
        User currentUser = userService.getCurrentUser();
        validateUser(currentUser);

        expense.setUser(currentUser);
        return expenseRepository.save(expense);
    }

    @Override
    public boolean deleteExpense(Long expenseId) {
        if (expenseRepository.existsById(expenseId)) {
            expenseRepository.deleteById(expenseId);
            return true;
        }
        return false;
    }

    @Override
    public List<Expense> getAllExpensesForUser() {
        User currentUser = userService.getCurrentUser();
        validateUser(currentUser);
        return expenseRepository.findAllByUserId(currentUser.getId());
    }

    @Override
    public String getTotalExpensesBetweenDatesForUser(LocalDate startDate, LocalDate endDate) {
        User currentUser = userService.getCurrentUser();
        validateUser(currentUser);
        Timestamp[] timestamps = convertToTimestamps(startDate, endDate);
        List<Expense> expenses = expenseRepository.findAllByUserIdAndDateBetween(currentUser.getId(), timestamps[0], timestamps[1]);

        BigDecimal totalAmount = expenses.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return "{\"totalAmount\": " + totalAmount + "}";
    }

    @Override
    public String getTotalExpensesBetweenDatesByCategoryForUser(LocalDate startDate, LocalDate endDate, String category) {
        User currentUser = userService.getCurrentUser();
        validateUser(currentUser);
        Timestamp[] timestamps = convertToTimestamps(startDate, endDate);
        List<Expense> expenses = expenseRepository.findAllByUserIdAndDateBetweenAndCategory(currentUser.getId(), timestamps[0], timestamps[1], category);

        BigDecimal totalAmount = expenses.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return "{\"totalAmount\": " + totalAmount + "}";
    }

    private void validateUser(User user) {
        if (user == null) {
            throw new RuntimeException("No logged-in user found");
        }
    }

    private Timestamp[] convertToTimestamps(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atStartOfDay().plusHours(23).plusMinutes(59).plusSeconds(59);

        Timestamp startTimestamp = Timestamp.valueOf(startDateTime);
        Timestamp endTimestamp = Timestamp.valueOf(endDateTime);

        return new Timestamp[]{startTimestamp, endTimestamp};
    }
}