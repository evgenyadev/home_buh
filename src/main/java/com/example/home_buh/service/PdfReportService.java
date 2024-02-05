package com.example.home_buh.service;

import com.example.home_buh.model.Expense;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PdfReportService {

    public byte[] generatePdfReport(List<Expense> expenses) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            PdfDocument pdfDocument = new PdfDocument(new PdfWriter(outputStream));
            Document document = new Document(pdfDocument);

            // Добавление содержимого в PDF документ, например, таблицы с расходами
            Table table = new Table(4);
            table.setWidth(500);

            table.addCell("ID");
            table.addCell("Amount");
            table.addCell("Date");
            table.addCell("Category");

            for (Expense expense : expenses) {
                table.addCell(String.valueOf(expense.getId()));
                table.addCell(String.valueOf(expense.getAmount()));
                table.addCell(expense.getDate().toString());
                table.addCell(expense.getCategory());
            }

            document.add(table);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return outputStream.toByteArray();
    }
}
