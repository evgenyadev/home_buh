package com.example.home_buh.controller;
import com.example.home_buh.model.User;
import com.example.home_buh.model.dto.ExpenseDTO;
import com.example.home_buh.service.ExpenseService;
import com.example.home_buh.service.PdfReportService;
import com.example.home_buh.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PDFController {

    private static final Logger logger = LoggerFactory.getLogger(PDFController.class);

    private final PdfReportService pdfReportService;
    private final ExpenseService expenseService;
    private final UserService userService;

    @Autowired
    public PDFController(PdfReportService pdfReportService, ExpenseService expenseService, UserService userService) {
        this.pdfReportService = pdfReportService;
        this.expenseService = expenseService;
        this.userService = userService;
    }

    @GetMapping("/download-pdf")
    public ResponseEntity<byte[]> downloadPDFReport() {
        try {
            User currentUser = userService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            List<ExpenseDTO> expenses = expenseService.getAllExpensesForUser(currentUser);

            // Генерация PDF отчета на основе списка расходов
            byte[] pdfBytes = pdfReportService.generatePdfReport(expenses);

            // Установка заголовков для ответа
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "report.pdf");
            headers.setContentLength(pdfBytes.length);

            // Возвращаем PDF как массив байтов в ответе
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("An error occurred", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
