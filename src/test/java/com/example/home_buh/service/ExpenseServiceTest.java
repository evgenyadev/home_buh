package com.example.home_buh.service;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.example.home_buh.service.ExpenseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.home_buh.model.Expense;
import com.example.home_buh.model.User;
import com.example.home_buh.repository.ExpenseRepository;
import com.example.home_buh.service.ExpenseService;
import com.example.home_buh.service.UserServiceImpl;

public class ExpenseServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private ExpenseService expenseService = new ExpenseServiceImpl(expenseRepository, userService);

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        userService = mock(UserServiceImpl.class);
        expenseService = new ExpenseServiceImpl(expenseRepository, userService);
    }

    @Test
    public void testCreateExpense() {
        Expense expense = new Expense();
        User user = new User();
        user.setId(1L);

        when(userService.getCurrentUser()).thenReturn(user);
        when(expenseRepository.save(expense)).thenReturn(expense);

        Expense createdExpense = expenseService.createExpense(expense);

        assertNotNull(createdExpense);
        assertEquals(user.getId(), createdExpense.getUser().getId());
        verify(expenseRepository, times(1)).save(expense);
    }

    @Test
    public void testDeleteExpense() {
        // Создаем тестовый расход
        Expense expense = new Expense();
        expense.setId(3L);

        // Предположим, что удаление расхода с id = 1L прошло успешно
        when(expenseRepository.existsById(1L)).thenReturn(true);
        doNothing().when(expenseRepository).deleteById(1L);

        // Вызываем метод deleteExpense и проверяем результат
        boolean deleted = expenseService.deleteExpense(1L);
        assertTrue(deleted);

        // Проверяем, что методы взаимодействия с репозиторием были вызваны правильно
        verify(expenseRepository, times(1)).existsById(1L);
        verify(expenseRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testGetAllExpensesForUser() {
        User user = new User();
        user.setId(1L);
        List<Expense> expenses = new ArrayList<>();
        expenses.add(new Expense());
        expenses.add(new Expense());

        when(userService.getCurrentUser()).thenReturn(user);
        when(expenseRepository.findAllByUserId(user.getId())).thenReturn(expenses);

        List<Expense> fetchedExpenses = expenseService.getAllExpensesForUser();

        assertNotNull(fetchedExpenses);
        assertEquals(expenses.size(), fetchedExpenses.size());
    }

    @Test
    public void testGetTotalExpensesBetweenDatesForUser() {
        User user = new User();
        user.setId(1L);
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 31);

        when(userService.getCurrentUser()).thenReturn(user);
        Timestamp startTimestamp = Timestamp.valueOf(startDate.atStartOfDay());
        Timestamp endTimestamp = Timestamp.valueOf(endDate.atStartOfDay().plusHours(23).plusMinutes(59).plusSeconds(59));

        when(expenseRepository.findAllByUserIdAndDateBetween(user.getId(), startTimestamp, endTimestamp)).thenReturn(new ArrayList<>());
        // Подготовка тестовых данных
        List<Expense> expenses = new ArrayList<>();
        // Добавление тестовых расходов, которые должны попасть в период
        // Это пример, как вы можете подготовить данные, они могут отличаться в вашем случае
        expenses.add(new Expense(user, new BigDecimal("10.00"), Timestamp.valueOf("2024-01-01 14:00:00").toLocalDateTime(), "Food"));
        expenses.add(new Expense(user, new BigDecimal("20.00"), Timestamp.valueOf("2024-01-02 15:00:00").toLocalDateTime(), "Food"));
        expenses.add(new Expense(user, new BigDecimal("30.00"), Timestamp.valueOf("2024-01-03 17:00:00").toLocalDateTime(), "Transportation"));

        // Настройка поведения репозитория для возврата тестовых расходов
        when(expenseRepository.findAllByUserIdAndDateBetween(user.getId(), startTimestamp, endTimestamp))
                .thenReturn(expenses);

        // Вызов тестируемого метода
        String totalAmount = expenseService.getTotalExpensesBetweenDatesForUser(startDate, endDate);
        assertNotNull(totalAmount);

        // Проверка результата
        assertEquals("{\"totalAmount\": 60.00}", totalAmount);
    }

    @Test
    public void testGetTotalExpensesBetweenDatesByCategoryForUser() {
        // Подготовка тестовых данных
        User user = new User();
        user.setId(1L);
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 31);
        String category = "Food";

        // Настройка поведения мок-объектов
        when(userService.getCurrentUser()).thenReturn(user);
        Timestamp startTimestamp = Timestamp.valueOf(startDate.atStartOfDay());
        Timestamp endTimestamp = Timestamp.valueOf(endDate.atStartOfDay().plusHours(23).plusMinutes(59).plusSeconds(59));

        // Подготовка тестовых расходов, которые должны попасть в период и соответствуют категории
        List<Expense> expenses = new ArrayList<>();
        // Это пример, как вы можете подготовить данные, они могут отличаться в вашем случае
        expenses.add(new Expense(user, new BigDecimal("20.00"), Timestamp.valueOf("2024-01-02 16:00:00").toLocalDateTime(), "Food"));
        expenses.add(new Expense(user, new BigDecimal("30.00"), Timestamp.valueOf("2024-01-03 18:00:00").toLocalDateTime(), "Food"));

        // Настройка поведения репозитория для возврата тестовых расходов
        when(expenseRepository.findAllByUserIdAndDateBetweenAndCategory(user.getId(), startTimestamp, endTimestamp, category))
                .thenReturn(expenses);

        // Вызов тестируемого метода
        String totalAmount = expenseService.getTotalExpensesBetweenDatesByCategoryForUser(startDate, endDate, category);
        assertNotNull(totalAmount);

        // Проверка результата
        assertEquals("{\"totalAmount\": 50.00}", totalAmount);

    }

}
