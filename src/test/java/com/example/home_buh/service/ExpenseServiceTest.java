package com.example.home_buh.service;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.home_buh.mapper.ExpenseMapper;
import com.example.home_buh.model.dto.ExpenseDTO;
import com.example.home_buh.model.dto.TotalExpenseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.example.home_buh.model.Expense;
import com.example.home_buh.model.User;
import com.example.home_buh.repository.ExpenseRepository;

class ExpenseServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private UserService userService;

    @Mock
    private ExpenseMapper expenseMapper;

    // @InjectMocks
    private ExpenseService expenseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this)
        expenseRepository = Mockito.mock(ExpenseRepository.class); // Создание mock объекта
        expenseMapper = new ExpenseMapper() {
            @Override
            public ExpenseDTO toExpenseDTO(Expense expense) {
                // Реализация преобразования объекта Expense в объект ExpenseDTO
                ExpenseDTO expenseDTO = new ExpenseDTO();
                expenseDTO.setId(expense.getId());
                expenseDTO.setAmount(expense.getAmount());
                expenseDTO.setDate(expense.getDate());
                expenseDTO.setCategory(expense.getCategory());
                return expenseDTO;
            }

            @Override
            public List<ExpenseDTO> toExpenseDTOs(List<Expense> expenses) {
                // Реализация преобразования списка Expense в список ExpenseDTO
                List<ExpenseDTO> expenseDTOs = new ArrayList<>();
                for (Expense expense : expenses) {
                    expenseDTOs.add(toExpenseDTO(expense));
                }
                return expenseDTOs;
            }
        }; // Инициализация mapper
        expenseService = new ExpenseServiceImpl(expenseRepository, expenseMapper);
    }

    @Test
    void testCreateExpense() {
        // Создаем мок пользователя
        User user = new User();
        user.setId(1L);

        // Создаем мок объекта расхода DTO
        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setId(1L);
        expenseDTO.setAmount(new BigDecimal("50.00"));
        expenseDTO.setDate(LocalDateTime.of(2024, 1, 15, 8, 0));
        expenseDTO.setCategory("Food");

        // Создаем объект Expense, который будет сохранен
        Expense expenseToSave = new Expense();
        expenseToSave.setUser(user);
        expenseToSave.setAmount(expenseDTO.getAmount());
        expenseToSave.setDate(expenseDTO.getDate());
        expenseToSave.setCategory(expenseDTO.getCategory());

        // Создаем мок объекта расхода, который будет возвращен после сохранения
        Expense savedExpense = new Expense();
        savedExpense.setId(1L);
        savedExpense.setUser(user);
        savedExpense.setAmount(expenseDTO.getAmount());
        savedExpense.setDate(expenseDTO.getDate());
        savedExpense.setCategory(expenseDTO.getCategory());

        // Настройка моков
        when(userService.getCurrentUser()).thenReturn(user);
        when(expenseRepository.save(any(Expense.class))).thenReturn(savedExpense);
        // when(expenseMapper.toExpenseDTO(savedExpense)).thenReturn(expenseDTO);

        // Вызываем метод createExpense() и сохраняем результат
        ExpenseDTO createdExpense = expenseService.createExpense(user, expenseDTO);

        // Проверяем, что расход был сохранен и возвращен корректно
        assertEquals(savedExpense.getId(), createdExpense.getId());
        assertEquals(savedExpense.getUser(), user);
        assertEquals(savedExpense.getAmount(), expenseDTO.getAmount());
        assertEquals(savedExpense.getDate(), expenseDTO.getDate());
        assertEquals(savedExpense.getCategory(), expenseDTO.getCategory());
    }

    @Test
    void testDeleteExpense() {
        // Подготовка тестовых данных
        User user = new User();
        user.setId(1L);
        Long expenseId = 1L;

        // Создание mock объекта расхода
        Expense expense = new Expense();
        expense.setId(expenseId);
        expense.setUser(user);
        expense.getUser().setId(2L);

        // Настройка mock объектов
        when(expenseRepository.findById(expenseId)).thenReturn(Optional.of(expense));

        // Вызов тестируемого метода
        boolean isDeleted = expenseService.deleteExpense(user, expenseId);

        // Проверка, что метод delete() был вызван один раз с объектом расхода
        verify(expenseRepository, times(1)).delete(expense);

        // Проверка, что расход был успешно удален
        assertTrue(isDeleted);
    }

    @Test
    void testGetAllExpensesForUser() {
        // Создаем пользователя для теста
        User user = new User();
        user.setId(1L);

        // Создаем список расходов для пользователя
        List<Expense> expenses = new ArrayList<>();
        expenses.add(new Expense(1L, user, new BigDecimal("20.00"), LocalDateTime.of(2024, 1, 5, 12, 0), "Food"));
        expenses.add(new Expense(2L, user, new BigDecimal("30.00"), LocalDateTime.of(2024, 1, 10, 10, 0), "Food"));

        // Устанавливаем поведение для mock expenseRepository
        when(expenseRepository.findAllByUserId(user.getId())).thenReturn(expenses);

        // Создаем список ExpenseDTO для теста
        List<ExpenseDTO> expenseDTOs = new ArrayList<>();
        expenseDTOs.add(new ExpenseDTO(1L, new BigDecimal("20.00"), LocalDateTime.of(2024, 1, 5, 12, 0), "Food"));
        expenseDTOs.add(new ExpenseDTO(2L, new BigDecimal("30.00"), LocalDateTime.of(2024, 1, 10, 10, 0), "Food"));

        // Устанавливаем поведение для mock expenseMapper
        // when(expenseMapper.toExpenseDTOs(expenses)).thenReturn(expenseDTOs);

        // Вызываем метод, который тестируем
        List<ExpenseDTO> result = expenseService.getAllExpensesForUser(user);

        // Проверяем, что результат не равен null
        assertNotNull(result);

        // Проверяем, что размер списков совпадает
        assertEquals(expenses.size(), result.size());

        // Проверяем, что метод findAllByUserId был вызван ровно один раз
        verify(expenseRepository, times(1)).findAllByUserId(user.getId());

        // Проверяем, что метод toExpenseDTOs был вызван ровно один раз
        // verify(expenseMapper, times(1)).toExpenseDTOs(expenses);
    }

    @Test
    void testGetTotalExpensesBetweenDatesForUser() {
        // Подготовка тестовых данных
        User user = new User();
        user.setId(1L);
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 31);

        // Создание тестовых расходов
        List<Expense> expenses = new ArrayList<>();
        expenses.add(new Expense(user, new BigDecimal("10.00"), LocalDateTime.of(2024, 1, 5, 12, 0), "Food"));
        expenses.add(new Expense(user, new BigDecimal("20.00"), LocalDateTime.of(2024, 1, 10, 10, 0), "Transportation"));
        expenses.add(new Expense(user, new BigDecimal("30.00"), LocalDateTime.of(2024, 1, 15, 8, 0), "Shopping"));
        // Добавляем дополнительную запись с датой, которая не попадает в диапазон
        expenses.add(new Expense(user, new BigDecimal("40.00"), LocalDateTime.of(2024, 2, 2, 12, 0), "Entertainment"));

        // Настройка моков
        when(userService.getCurrentUser()).thenReturn(user);
        LocalDateTime startTimestamp = startDate.atStartOfDay();
        LocalDateTime endTimestamp = endDate.atTime(23, 59, 59);

        List<Expense> filteredExpenses = expenses.stream()
                .filter(expense -> expense.getDate().toLocalDate().isEqual(startDate) ||
                        expense.getDate().toLocalDate().isEqual(endDate) ||
                        (expense.getDate().toLocalDate().isAfter(startDate) &&
                                expense.getDate().toLocalDate().isBefore(endDate)))
                .collect(Collectors.toList());

        when(expenseRepository.findAllByUserIdAndDateBetween(user.getId(), startTimestamp, endTimestamp))
                .thenReturn(filteredExpenses);

        // Создаем список с тремя ExpenseDTO
//        List<ExpenseDTO> filteredExpenseDTOs = new ArrayList<>();
//        IntStream.range(0, 3).forEach(i -> {
//            ExpenseDTO expenseDTO = new ExpenseDTO();
//            // Здесь можно установить значения для ExpenseDTO, если это необходимо
//            filteredExpenseDTOs.add(expenseDTO);
//        });

        List<ExpenseDTO> filteredExpenseDTOs = expenseMapper.toExpenseDTOs(filteredExpenses);

        // when(expenseMapper.toExpenseDTOs(filteredExpenses)).thenReturn(filteredExpenseDTOs);
        // Вызов тестируемого метода
        TotalExpenseDTO totalAmount = expenseService.getTotalExpensesBetweenDatesForUser(user, startDate, endDate);

        // Проверка результата
        assertEquals(filteredExpenseDTOs.size(), totalAmount.getExpenses().size());
        assertEquals(new BigDecimal("60.00"), totalAmount.getTotalAmount()); // Сумма всех расходов в выборке
    }

    @Test
    void testGetTotalExpensesBetweenDatesByCategoryForUser() {
        // Подготовка тестовых данных
        User user = new User();
        user.setId(1L);
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 31);
        String category = "Food";

        // Создание тестовых расходов
        List<Expense> expenses = new ArrayList<>();
        expenses.add(new Expense(user, new BigDecimal("10.00"), LocalDateTime.of(2024, 1, 5, 12, 0), "Food"));
        expenses.add(new Expense(user, new BigDecimal("20.00"), LocalDateTime.of(2024, 1, 10, 10, 0), "Food"));
        expenses.add(new Expense(user, new BigDecimal("30.00"), LocalDateTime.of(2024, 1, 15, 8, 0), "Shopping"));
        // Добавляем еще один расход с другой категорией
        expenses.add(new Expense(user, new BigDecimal("40.00"), LocalDateTime.of(2024, 1, 20, 12, 0), "Transportation"));

        // Настройка моков
        when(userService.getCurrentUser()).thenReturn(user);
        LocalDateTime startTimestamp = startDate.atStartOfDay();
        LocalDateTime endTimestamp = endDate.atTime(23, 59, 59);

        List<Expense> filteredExpenses = expenses.stream()
                .filter(expense -> expense.getCategory().equals(category))
                .filter(expense -> expense.getDate().toLocalDate().isEqual(startDate) ||
                        expense.getDate().toLocalDate().isEqual(endDate) ||
                        (expense.getDate().toLocalDate().isAfter(startDate) &&
                                expense.getDate().toLocalDate().isBefore(endDate)))
                .collect(Collectors.toList());

        when(expenseRepository.findAllByUserIdAndDateBetweenAndCategory(user.getId(), startTimestamp, endTimestamp, category))
                .thenReturn(filteredExpenses);

        // Создаем список с двумя ExpenseDTO
//        List<ExpenseDTO> filteredExpenseDTOs = new ArrayList<>();
//        IntStream.range(0, 2).forEach(i -> {
//            ExpenseDTO expenseDTO = new ExpenseDTO();
//            // Здесь можно установить значения для ExpenseDTO, если это необходимо
//            filteredExpenseDTOs.add(expenseDTO);
//        });

        List<ExpenseDTO> filteredExpenseDTOs = expenseMapper.toExpenseDTOs(filteredExpenses);

//        when(expenseMapper.toExpenseDTOs(filteredExpenses)).thenReturn(filteredExpenseDTOs);

        // Вызов тестируемого метода
        TotalExpenseDTO totalAmount = expenseService.getTotalExpensesBetweenDatesByCategoryForUser(user, startDate, endDate, category);

        // Проверка результата
        assertEquals(filteredExpenseDTOs.size(), totalAmount.getExpenses().size());
        assertEquals(new BigDecimal("30.00"), totalAmount.getTotalAmount()); // Сумма всех расходов в выборке категории "Food"
    }

}
