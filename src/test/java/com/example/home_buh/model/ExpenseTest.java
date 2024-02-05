package com.example.home_buh.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ExpenseTest {

    @Test
    public void testExpenseModel() {
        // Создаем пользователя
        User user = new User();
        user.setId(1L);

        // Создаем объект Expense
        LocalDateTime now = LocalDateTime.now();
        BigDecimal amount = new BigDecimal("100.0");
        String category = "Food";
        Expense expense = new Expense(user, amount, now, category);

        // Проверяем геттеры
        assertEquals(user, expense.getUser());
        assertEquals(amount, expense.getAmount());
        assertEquals(now, expense.getDate());
        assertEquals(category, expense.getCategory());

        // Меняем значения и проверяем сеттеры
        User newUser = new User();
        newUser.setId(2L);
        LocalDateTime newDate = LocalDateTime.now().minusDays(1);
        BigDecimal newAmount = new BigDecimal("200.0");
        String newCategory = "Transport";

        expense.setUser(newUser);
        expense.setAmount(newAmount);
        expense.setDate(newDate);
        expense.setCategory(newCategory);

        assertEquals(newUser, expense.getUser());
        assertEquals(newAmount, expense.getAmount());
        assertEquals(newDate, expense.getDate());
        assertEquals(newCategory, expense.getCategory());

        // Проверяем конструктор
        Expense newExpense = new Expense();
        assertNull(newExpense.getId());
        assertNull(newExpense.getUser());
        assertNull(newExpense.getAmount());
        assertNull(newExpense.getDate());
        assertNull(newExpense.getCategory());
    }
}
