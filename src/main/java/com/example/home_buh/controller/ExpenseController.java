package com.example.home_buh.controller;

import com.example.home_buh.model.User;
import com.example.home_buh.model.dto.ExpenseDTO;
import com.example.home_buh.model.dto.TotalExpenseDTO;
import com.example.home_buh.service.ExpenseService;
import com.example.home_buh.service.UserService;
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
    private final UserService userService;

    @Autowired
    public ExpenseController(ExpenseService expenseService, UserService userService) {
        this.expenseService = expenseService;
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createExpense(@RequestBody ExpenseDTO expenseDTO) {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
        ExpenseDTO savedExpense = expenseService.createExpense(currentUser, expenseDTO);
        if (savedExpense != null) {
            return new ResponseEntity<>("Expense created successfully", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Failed to create expense", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{expenseId}")
    public ResponseEntity<String> deleteExpense(@PathVariable Long expenseId) {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        boolean deleted = expenseService.deleteExpense(currentUser, expenseId);
        if (deleted) {
            return ResponseEntity.ok("Expense deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Expense not found or does not belong to the user");
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Map<String, List<ExpenseDTO>>> getAllExpensesForUser() {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<ExpenseDTO> userExpenses = expenseService.getAllExpensesForUser(currentUser);

        Map<String, List<ExpenseDTO>> expenses = new HashMap<>();
        expenses.put("expenses", userExpenses);

        return new ResponseEntity<>(expenses, HttpStatus.OK);
    }

    @GetMapping("/between-dates")
    public ResponseEntity<TotalExpenseDTO> getExpensesBetweenDates(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        TotalExpenseDTO expenses = expenseService.getTotalExpensesBetweenDatesForUser(currentUser, startDate, endDate);
        return ResponseEntity.ok(expenses);
    }


    @GetMapping("/between-dates-by-category")
    public ResponseEntity<TotalExpenseDTO> getExpensesBetweenDatesByCategory(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam("category") String category) {

        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        TotalExpenseDTO expenses = expenseService.getTotalExpensesBetweenDatesByCategoryForUser(currentUser, startDate, endDate, category);
        return ResponseEntity.ok(expenses);
    }
}