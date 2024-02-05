package com.example.home_buh.repository;

import com.example.home_buh.model.Expense;
import com.example.home_buh.model.User;
import com.example.home_buh.service.ExpenseServiceImpl;
import com.example.home_buh.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExpenseRepositoryTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private ExpenseServiceImpl expenseService;

    @Test
    public void testFindAllByUserId() {
        // Создание мок-списка расходов
        List<Expense> expenses = new ArrayList<>();
        User user = new User();
        user.setId(1L);
        expenses.add(new Expense(user, new BigDecimal("100.0"), LocalDateTime.now(), "Food"));
        expenses.add(new Expense(user, new BigDecimal("200.0"), LocalDateTime.now(), "Transport"));

        // Настройка UserService
        when(userService.getCurrentUser()).thenReturn(user);

        // Устанавливаем поведение мок-репозитория expenseRepository
        when(expenseRepository.findAllByUserId(1L)).thenReturn(expenses);

        // Вызываем метод поиска расходов по идентификатору пользователя
        List<Expense> result = expenseService.getAllExpensesForUser();

        // Проверяем, что результат содержит все расходы пользователя с указанным идентификатором
        assertEquals(expenses.size(), result.size());
        assertEquals(expenses, result);
    }

    @Test
    public void testFindAllByUserIdAndDateBetween() {
        // Создание мок-списка расходов
        List<Expense> expenses = new ArrayList<>();
        User user = new User();
        user.setId(1L);
        expenses.add(new Expense(user, new BigDecimal("100.0"), LocalDateTime.now(), "Food"));
        expenses.add(new Expense(user, new BigDecimal("200.0"), LocalDateTime.now(), "Transport"));

        // Определение startTime и endTime как LocalDate
        LocalDate startTime = LocalDate.now().minusDays(1);
        LocalDate endTime = LocalDate.now();

        // Настройка UserService
        when(userService.getCurrentUser()).thenReturn(user);

        // Устанавливаем поведение мок-репозитория expenseRepository
        when(expenseRepository.findAllByUserIdAndDateBetween(
                eq(user.getId()),
                any(Timestamp.class),
                any(Timestamp.class)))
                .thenReturn(expenses);

        // Вызов метода поиска расходов по идентификатору пользователя и дате
        String result = expenseService.getTotalExpensesBetweenDatesForUser(startTime, endTime);

        // Проверка строки JSON на корректность
        assertEquals("{\"totalAmount\": 300.0}", result);
    }

    @Test
    public void testFindAllByUserIdAndDateBetweenAndCategory() {
        // Создание мок-списка расходов
        List<Expense> expenses = new ArrayList<>();
        User user = new User();
        user.setId(1L);
        expenses.add(new Expense(user, new BigDecimal("100.0"), LocalDateTime.now(), "Food"));
        expenses.add(new Expense(user, new BigDecimal("200.0"), LocalDateTime.now(), "Transport"));

        // Определение startTime и endTime как LocalDate
        LocalDate startTime = LocalDate.now().minusDays(1);
        LocalDate endTime = LocalDate.now();

        LocalDateTime endOfDay = endTime.atTime(LocalTime.MAX);

        // Настройка UserService
        when(userService.getCurrentUser()).thenReturn(user);

        // Устанавливаем поведение мок-репозитория expenseRepository
        when(expenseRepository.findAllByUserIdAndDateBetweenAndCategory(
                eq(user.getId()),
                any(Timestamp.class),
                any(Timestamp.class),
                eq("Food")))
                .thenReturn(Collections.singletonList(expenses.get(0))); // Только один расход в категории "Food"

        // Вызов метода поиска расходов по идентификатору пользователя, дате и категории
        String result = expenseService.getTotalExpensesBetweenDatesByCategoryForUser(startTime, endTime, "Food");

        // Проверка строки JSON на корректность
        assertEquals("{\"totalAmount\": 100.0}", result);
    }

}
