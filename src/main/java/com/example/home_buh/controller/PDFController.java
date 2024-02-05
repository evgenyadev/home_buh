package com.example.home_buh.controller;
import com.example.home_buh.model.Expense;
import com.example.home_buh.service.ExpenseService;
import com.example.home_buh.service.PdfReportService;
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

    private final PdfReportService pdfReportService;
    private final ExpenseService expenseService;

    @Autowired
    public PDFController(PdfReportService pdfReportService, ExpenseService expenseService) {
        this.pdfReportService = pdfReportService;
        this.expenseService = expenseService;
    }

    @GetMapping("/download-pdf")
    public ResponseEntity<byte[]> downloadPDFReport() {
        try {
            List<Expense> expenses = expenseService.getAllExpensesForUser();

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
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
