package com.example.home_buh.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import com.example.home_buh.mapper.ExpenseMapper;
import com.example.home_buh.model.Expense;
import com.example.home_buh.model.User;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.layout.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

class PdfReportServiceTest {

    private ExpenseMapper expenseMapper;

    @BeforeEach
    void setUp() {
        expenseMapper = mock(ExpenseMapper.class);
    }

    @Test
    void testGeneratePdfReport() throws IOException {
        // Создаем пользователя
        User user = new User();
        user.setId(1L); // Присваиваем id пользователя

        // Создаем список тестовых расходов и добавляем две записи
        List<Expense> expenses = Arrays.asList(
                new Expense(user, new BigDecimal("10.00"), LocalDateTime.of(2024, 2, 6, 10, 30), "Food"),
                new Expense(user, new BigDecimal("20.00"), LocalDateTime.of(2024, 2, 7, 12, 0), "Transportation")
        );

        // Создаем объект PdfReportService
        PdfReportService pdfReportService = new PdfReportService();

        // Генерируем PDF-отчет
        byte[] pdfBytes = pdfReportService.generatePdfReport(expenseMapper.toExpenseDTOs(expenses));

        // Проверяем, что PDF-отчет не пустой
        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 0);

        // Проверяем, что содержимое PDF-отчета соответствует ожидаемому формату
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(pdfBytes)) {
            Document document = new Document(new PdfDocument(new PdfReader(inputStream)));
            document.close();
            // Проверка размера созданного PDF файла
            assertTrue(pdfBytes.length > 0);
        }
    }
}
