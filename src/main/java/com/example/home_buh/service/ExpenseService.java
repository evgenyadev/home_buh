package com.example.home_buh.service;

import com.example.home_buh.model.Expense;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseService {

    Expense createExpense(Expense expense);

    boolean deleteExpense(Long expenseId);

    List<Expense> getAllExpensesForUser();

    String getTotalExpensesBetweenDatesForUser(LocalDate startDate, LocalDate endDate);

    String getTotalExpensesBetweenDatesByCategoryForUser(LocalDate startDate, LocalDate endDate, String category);
}