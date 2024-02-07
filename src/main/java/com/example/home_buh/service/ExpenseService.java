package com.example.home_buh.service;

import com.example.home_buh.model.User;
import com.example.home_buh.model.dto.ExpenseDTO;
import com.example.home_buh.model.dto.TotalExpenseDTO;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseService {

    ExpenseDTO createExpense(User user, ExpenseDTO expenseDTO);

    boolean deleteExpense(User user, Long expenseId);

    List<ExpenseDTO> getAllExpensesForUser(User user);

    TotalExpenseDTO getTotalExpensesBetweenDatesForUser(User user, LocalDate startDate, LocalDate endDate);

    TotalExpenseDTO getTotalExpensesBetweenDatesByCategoryForUser(User user, LocalDate startDate, LocalDate endDate, String category);
}