package com.example.home_buh.controller;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.home_buh.model.User;
import com.example.home_buh.model.dto.ExpenseDTO;
import com.example.home_buh.model.dto.TotalExpenseDTO;
import com.example.home_buh.service.ExpenseService;
import com.example.home_buh.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

class ExpenseControllerTest {

    @Mock
    private ExpenseService expenseService;

    @Mock
    private UserService userService;

    @InjectMocks
    private ExpenseController expenseController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateExpense_Success() {
        // Mocking
        ExpenseDTO expenseDTO = new ExpenseDTO();
        // Устанавливаем, что при вызове userService.getCurrentUser() будет возвращаться не null
        when(userService.getCurrentUser()).thenReturn(new User());
        // Устанавливаем, что при вызове expenseService.createExpense() будет возвращаться не null
        when(expenseService.createExpense(any(User.class), eq(expenseDTO))).thenReturn(expenseDTO);

        // Вызываем метод контроллера для создания расхода
        ResponseEntity<String> responseEntity = expenseController.createExpense(expenseDTO);

        // Проверяем успешное создание расхода
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("Expense created successfully", responseEntity.getBody());
    }

    @Test
    void testCreateExpense_Failure() {
        // Mocking
        ExpenseDTO expenseDTO = new ExpenseDTO();
        // Устанавливаем, что при вызове userService.getCurrentUser() будет возвращаться не null
        when(userService.getCurrentUser()).thenReturn(new User());
        // Устанавливаем, что при вызове expenseService.createExpense() будет возвращаться null
        when(expenseService.createExpense(any(User.class), eq(expenseDTO))).thenReturn(null);

        // Вызываем метод контроллера для создания расхода
        ResponseEntity<String> responseEntity = expenseController.createExpense(expenseDTO);

        // Проверяем, что расход не был создан успешно
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Failed to create expense", responseEntity.getBody());
    }

    @Test
    void testCreateExpense_Unauthorized() {
        // Mocking
        ExpenseDTO expenseDTO = new ExpenseDTO();
        // Устанавливаем, что при вызове userService.getCurrentUser() будет возвращаться null
        when(userService.getCurrentUser()).thenReturn(null);

        // Вызываем метод контроллера для создания расхода
        ResponseEntity<String> responseEntity = expenseController.createExpense(expenseDTO);

        // Проверяем, что пользователь не аутентифицирован
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertEquals("User not authenticated", responseEntity.getBody());
    }

    @Test
    void testDeleteExpense_Success() {
        // Mocking
        Long expenseId = 1L;
        // Устанавливаем, что при вызове userService.getCurrentUser() будет возвращаться не null
        when(userService.getCurrentUser()).thenReturn(new User());
        // Устанавливаем, что при вызове expenseService.deleteExpense() будет возвращаться true
        when(expenseService.deleteExpense(any(User.class), eq(expenseId))).thenReturn(true);

        // Вызываем метод контроллера для удаления расхода
        ResponseEntity<String> responseEntity = expenseController.deleteExpense(expenseId);

        // Проверяем успешное удаление расхода
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Expense deleted successfully", responseEntity.getBody());
    }

    @Test
    void testDeleteExpense_NotFound() {
        // Mocking
        Long expenseId = 1L;
        // Устанавливаем, что при вызове userService.getCurrentUser() будет возвращаться не null
        when(userService.getCurrentUser()).thenReturn(new User());
        // Устанавливаем, что при вызове expenseService.deleteExpense() будет возвращаться false
        when(expenseService.deleteExpense(any(User.class), eq(expenseId))).thenReturn(false);

        // Вызываем метод контроллера для удаления расхода
        ResponseEntity<String> responseEntity = expenseController.deleteExpense(expenseId);

        // Проверяем, что расход не найден или не принадлежит пользователю
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Expense not found or does not belong to the user", responseEntity.getBody());
    }

    @Test
    void testDeleteExpense_Unauthorized() {
        // Mocking
        Long expenseId = 1L;
        // Устанавливаем, что при вызове userService.getCurrentUser() будет возвращаться null
        when(userService.getCurrentUser()).thenReturn(null);

        // Вызываем метод контроллера для удаления расхода
        ResponseEntity<String> responseEntity = expenseController.deleteExpense(expenseId);

        // Проверяем, что пользователь не аутентифицирован
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertEquals("User not authenticated", responseEntity.getBody());
    }

    @Test
    void testGetAllExpensesForUser_Success() {
        // Mocking
        User currentUser = new User();
        List<ExpenseDTO> expectedExpenses = new ArrayList<>();
        expectedExpenses.add(new ExpenseDTO());
        expectedExpenses.add(new ExpenseDTO());

        // Устанавливаем, что при вызове userService.getCurrentUser() будет возвращаться не null
        when(userService.getCurrentUser()).thenReturn(currentUser);
        // Устанавливаем, что при вызове expenseService.getAllExpensesForUser() будет возвращаться список тестовых расходов
        when(expenseService.getAllExpensesForUser(currentUser)).thenReturn(expectedExpenses);

        // Вызываем метод контроллера для получения всех расходов пользователя
        ResponseEntity<Map<String, List<ExpenseDTO>>> responseEntity = expenseController.getAllExpensesForUser();

        // Проверяем успешное получение всех расходов пользователя
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedExpenses, Objects.requireNonNull(responseEntity.getBody()).get("expenses"));
    }

    @Test
    void testGetAllExpensesForUser_Unauthorized() {
        // Устанавливаем, что при вызове userService.getCurrentUser() будет возвращаться null
        when(userService.getCurrentUser()).thenReturn(null);

        // Вызываем метод контроллера для получения всех расходов пользователя
        ResponseEntity<Map<String, List<ExpenseDTO>>> responseEntity = expenseController.getAllExpensesForUser();

        // Проверяем, что пользователь не аутентифицирован
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }

    @Test
    void testGetExpensesBetweenDates_Success() {
        // Устанавливаем тестовые данные
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 5);
        User currentUser = new User();
        TotalExpenseDTO totalExpenseDTO = new TotalExpenseDTO();
        totalExpenseDTO.setTotalAmount(new BigDecimal("30.00"));
        List<ExpenseDTO> expenses = new ArrayList<>();
        totalExpenseDTO.setExpenses(expenses);

        // Устанавливаем, что при вызове userService.getCurrentUser() будет возвращаться не null
        when(userService.getCurrentUser()).thenReturn(currentUser);
        // Устанавливаем, что при вызове expenseService.getTotalExpensesBetweenDatesForUser() будет возвращаться totalExpenseDTO
        when(expenseService.getTotalExpensesBetweenDatesForUser(currentUser, startDate, endDate)).thenReturn(totalExpenseDTO);

        // Вызываем метод контроллера для получения расходов между датами
        ResponseEntity<TotalExpenseDTO> responseEntity = expenseController.getExpensesBetweenDates(startDate, endDate);

        // Проверяем успешное получение расходов между датами
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(totalExpenseDTO, responseEntity.getBody());
    }

    @Test
    void testGetExpensesBetweenDates_Unauthorized() {
        // Устанавливаем, что при вызове userService.getCurrentUser() будет возвращаться null
        when(userService.getCurrentUser()).thenReturn(null);

        // Вызываем метод контроллера для получения расходов между датами
        ResponseEntity<TotalExpenseDTO> responseEntity = expenseController.getExpensesBetweenDates(LocalDate.now(), LocalDate.now());

        // Проверяем, что пользователь не аутентифицирован
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }

    @Test
    void testGetExpensesBetweenDatesByCategory_Success() {
        // Устанавливаем тестовые данные
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 5);
        String category = "Food";
        User currentUser = new User();
        TotalExpenseDTO totalExpenseDTO = new TotalExpenseDTO();
        totalExpenseDTO.setTotalAmount(new BigDecimal("30.00"));
        List<ExpenseDTO> expenses = new ArrayList<>();
        totalExpenseDTO.setExpenses(expenses);

        // Устанавливаем, что при вызове userService.getCurrentUser() будет возвращаться не null
        when(userService.getCurrentUser()).thenReturn(currentUser);
        // Устанавливаем, что при вызове expenseService.getTotalExpensesBetweenDatesByCategoryForUser() будет возвращаться totalExpenseDTO
        when(expenseService.getTotalExpensesBetweenDatesByCategoryForUser(currentUser, startDate, endDate, category)).thenReturn(totalExpenseDTO);

        // Вызываем метод контроллера для получения расходов между датами по категории
        ResponseEntity<TotalExpenseDTO> responseEntity = expenseController.getExpensesBetweenDatesByCategory(startDate, endDate, category);

        // Проверяем успешное получение расходов между датами по категории
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(totalExpenseDTO, responseEntity.getBody());
    }

    @Test
    void testGetExpensesBetweenDatesByCategory_Unauthorized() {
        // Устанавливаем, что при вызове userService.getCurrentUser() будет возвращаться null
        when(userService.getCurrentUser()).thenReturn(null);

        // Вызываем метод контроллера для получения расходов между датами по категории
        ResponseEntity<TotalExpenseDTO> responseEntity = expenseController.getExpensesBetweenDatesByCategory(LocalDate.now(), LocalDate.now(), "Food");

        // Проверяем, что пользователь не аутентифицирован
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }
}
