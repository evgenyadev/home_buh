package com.example.home_buh.controller;

import com.example.home_buh.mapper.ExpenseMapper;
import com.example.home_buh.model.Expense;
import com.example.home_buh.model.User;
import com.example.home_buh.model.dto.ExpenseDTO;
import com.example.home_buh.service.ExpenseService;
import com.example.home_buh.service.PdfReportService;
import com.example.home_buh.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class PDFControllerTest {

    @Mock
    private PdfReportService pdfReportService;

    @Mock
    private ExpenseService expenseService;

    @Mock
    private UserService userService;
    @InjectMocks
    private PDFController pdfController;
    private ExpenseMapper expenseMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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
    }

    @Test
    void testDownloadPDFReport_Success() {
        // Создаем тестовый список расходов
        List<Expense> expenses = new ArrayList<>();
        // Добавляем несколько тестовых расходов
        User user = new User();
        user.setId(1L);
        when(userService.getCurrentUser()).thenReturn(user);
        expenses.add(new Expense(user, new BigDecimal("10.00"), LocalDateTime.of(2024, 1, 2, 0, 0), "Food"));
        expenses.add(new Expense(user, new BigDecimal("20.00"), LocalDateTime.of(2024, 1, 3, 0, 0), "Transportation"));

        // Mocking: когда вызывается метод getAllExpensesForUser(), возвращается тестовый список расходов
        when(expenseService.getAllExpensesForUser(user)).thenReturn(expenseMapper.toExpenseDTOs(expenses));

        // Mocking: когда вызывается метод generatePdfReport(), возвращается тестовый массив байтов PDF
        byte[] pdfBytes = new byte[10]; // тестовые байты PDF
        when(pdfReportService.generatePdfReport(expenseMapper.toExpenseDTOs(expenses))).thenReturn(pdfBytes);

        // Вызываем метод контроллера, который мы хотим протестировать
        ResponseEntity<byte[]> responseEntity = pdfController.downloadPDFReport();

        // Проверяем, что ответ не null
        assertNotNull(responseEntity);

        // Проверяем, что код статуса ответа - HttpStatus.OK
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // Проверяем, что ответ имеет Content-Type: application/pdf
        assertEquals(MediaType.APPLICATION_PDF, responseEntity.getHeaders().getContentType());

        // Проверяем, что ответ содержит ожидаемый заголовок Content-Disposition
        ContentDisposition contentDisposition = responseEntity.getHeaders().getContentDisposition();
        assertEquals("form-data", contentDisposition.getType());
        assertEquals("filename", contentDisposition.getName());
        assertEquals("report.pdf", contentDisposition.getFilename());

        // Проверяем, что размер ответа соответствует размеру тестового PDF
        assertEquals(pdfBytes.length, Objects.requireNonNull(responseEntity.getBody()).length);
    }
}
