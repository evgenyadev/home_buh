package com.example.home_buh.controller;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import com.example.home_buh.controller.ExpenseController;
import com.example.home_buh.model.Expense;
import com.example.home_buh.model.User;
import com.example.home_buh.service.ExpenseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpenseControllerTest {

    @Mock
    private ExpenseService expenseService;

    @InjectMocks
    private ExpenseController expenseController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateExpense_Success() {
        // Mocking
        Expense expense = new Expense();
        when(expenseService.createExpense(expense)).thenReturn(expense);

        // Вызов метода контроллера для создания расхода
        ResponseEntity<String> responseEntity = expenseController.createExpense(expense);

        // Проверка успешного создания расхода
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("Expense created successfully", responseEntity.getBody());
    }

    @Test
    public void testDeleteExpense_Success() {
        // Mocking
        Long expenseId = 1L;
        when(expenseService.deleteExpense(expenseId)).thenReturn(true);

        // Создание объекта контроллера для тестирования
        ExpenseController controller = new ExpenseController(expenseService);

        // Вызов метода контроллера для удаления расхода
        ResponseEntity<String> responseEntity = controller.deleteExpense(expenseId);

        // Проверка успешного удаления расхода
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Expense deleted successfully", responseEntity.getBody());
    }

    @Test
    public void testGetAllExpensesForUser() {
        // Создаем тестируемый контроллер и передаем ему заглушку сервиса
        ExpenseController controller = new ExpenseController(expenseService);

        // Создаем список тестовых расходов
        List<Expense> expectedExpenses = new ArrayList<>();
        expectedExpenses.add(new Expense());
        expectedExpenses.add(new Expense());

        // Устанавливаем поведение заглушки: при вызове getAllExpensesForUser() возвращается список тестовых расходов
        when(expenseService.getAllExpensesForUser()).thenReturn(expectedExpenses);

        // Вызываем метод контроллера, который мы хотим протестировать
        ResponseEntity<?> responseEntity = controller.getAllExpensesForUser();

        // Проверяем, что ответ не равен null
        assertNotNull(responseEntity);

        // Проверяем, что код статуса ответа - HttpStatus.OK
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // Проверяем, что данные в ответе соответствуют ожидаемым данным
        List<Expense> actualExpenses = (List<Expense>) ((HashMap) responseEntity.getBody()).get("expenses");
        assertEquals(expectedExpenses.size(), actualExpenses.size());
    }

    @Test
    void testGetExpensesBetweenDates() {
        // Устанавливаем тестовые данные
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 5);

        List<Expense> expenses = new ArrayList<>();
        User user = new User();
        // Создание объектов Expense для записей внутри диапазона дат
        expenses.add(new Expense(user, new BigDecimal("10.00"), LocalDateTime.of(2024, 1, 2, 0, 0), "Food"));
        expenses.add(new Expense(user, new BigDecimal("20.00"), LocalDateTime.of(2024, 1, 3, 0, 0), "Car"));

        // Создание объектов Expense для записей за пределами диапазона дат
        expenses.add(new Expense(user, new BigDecimal("30.00"), LocalDateTime.of(2024, 1, 6, 0, 0), "Entertainment"));
        expenses.add(new Expense(user, new BigDecimal("40.00"), LocalDateTime.of(2024, 1, 7, 0, 0), "Cafe"));


        when(expenseService.getTotalExpensesBetweenDatesForUser(startDate, endDate))
                .thenReturn("{\"totalAmount\": 30.00}");

        // Вызываем метод контроллера
        ResponseEntity<String> responseEntity = expenseController.getExpensesBetweenDates(startDate, endDate);

        // Проверяем, что ответ не null
        assertNotNull(responseEntity);
        // Проверяем, что ответ имеет статус ОК
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        // Проверяем, что ответ содержит ожидаемую строку
        assertEquals("{\"totalAmount\": 30.00}", responseEntity.getBody());
    }

    @Test
    void testGetExpensesBetweenDatesByCategory() {
        // Устанавливаем тестовые данные
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 5);
        String category = "Food";

        // Устанавливаем сумму расходов в этой категории
        String totalAmount = "30.00";

        // Устанавливаем поведение заглушки: при вызове getTotalExpensesBetweenDatesByCategoryForUser() возвращается строка с суммой расходов
        when(expenseService.getTotalExpensesBetweenDatesByCategoryForUser(startDate, endDate, category))
                .thenReturn("{\"totalAmount\": \"" + totalAmount + "\"}");

        // Вызываем метод контроллера
        ResponseEntity<String> responseEntity = expenseController.getExpensesBetweenDatesByCategory(startDate, endDate, category);

        // Проверяем, что ответ не null
        assertNotNull(responseEntity);
        // Проверяем, что ответ имеет статус ОК
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // Преобразуем фактическое значение в строку JSON
        String expectedResponse = "{\"totalAmount\": \"" + totalAmount + "\"}";

        // Проверяем, что ответ содержит ожидаемую строку
        assertEquals(expectedResponse, responseEntity.getBody());
    }

}
