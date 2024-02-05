package com.example.home_buh.controller;

import com.example.home_buh.model.Expense;
import com.example.home_buh.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    @Autowired
    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createExpense(@RequestBody Expense expense) {
        Expense savedExpense = expenseService.createExpense(expense);
        if (savedExpense != null) {
            return new ResponseEntity<>("Expense created successfully", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Failed to create expense", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{expenseId}")
    public ResponseEntity<String> deleteExpense(@PathVariable Long expenseId) {
        boolean deleted = expenseService.deleteExpense(expenseId);
        if (deleted) {
            return ResponseEntity.ok("Expense deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Expense not found");
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Map<String, List<Expense>>> getAllExpensesForUser() {
        Map<String, List<Expense>> expenses = new HashMap<>();
        expenses.put("expenses", expenseService.getAllExpensesForUser());
        return new ResponseEntity<>(expenses, HttpStatus.OK);
    }

    @GetMapping("/between-dates")
    public ResponseEntity<String> getExpensesBetweenDates(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        String expenses = expenseService.getTotalExpensesBetweenDatesForUser(startDate, endDate);
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/between-dates-by-category")
    public ResponseEntity<String> getExpensesBetweenDatesByCategory(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam("category") String category) {

        String expensesByCategory = expenseService.getTotalExpensesBetweenDatesByCategoryForUser(startDate, endDate, category);
        return ResponseEntity.ok(expensesByCategory);
    }
}