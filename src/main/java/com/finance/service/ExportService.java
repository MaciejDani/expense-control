package com.finance.service;

import com.finance.model.Expense;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ExportService {

    public void exportExpensesToExcel(List<Expense> expenses, HttpServletResponse response) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Expenses");

        // Nagłówki kolumn
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "Category", "Amount", "Date", "Description", "Currency"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(createHeaderStyle(workbook));
        }

        // Dane o wydatkach
        int rowIdx = 1;
        for (Expense expense : expenses) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(expense.getId());
            row.createCell(1).setCellValue(expense.getCategory().getName());
            row.createCell(2).setCellValue(expense.getAmount().doubleValue());
            row.createCell(3).setCellValue(expense.getDate().toString());
            row.createCell(4).setCellValue(expense.getDescription());
            row.createCell(5).setCellValue(expense.getCurrency());
        }

        // Dopasowanie szerokości kolumn
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Ustawienie odpowiedzi HTTP
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=expenses.xlsx");

        workbook.write(response.getOutputStream());
        workbook.close();
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }
}
