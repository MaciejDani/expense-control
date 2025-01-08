package com.finance.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.finance.model.Category;
import com.finance.model.Expense;
import jakarta.servlet.ServletOutputStream;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import jakarta.servlet.http.HttpServletResponse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

class ExportServiceTest {

    private ExportService exportService;

    @BeforeEach
    void setUp() {
        exportService = new ExportService();
    }

    @Test
    void testExportExpensesToExcel() throws IOException {
        // Mockowanie odpowiedzi HTTP
        HttpServletResponse response = mock(HttpServletResponse.class);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Mockowanie `ServletOutputStream`
        ServletOutputStream servletOutputStream = mock(ServletOutputStream.class);
        doAnswer(invocation -> {
            byte[] buffer = invocation.getArgument(0);
            int offset = invocation.getArgument(1);
            int length = invocation.getArgument(2);
            outputStream.write(buffer, offset, length);
            return null;
        }).when(servletOutputStream).write(any(byte[].class), anyInt(), anyInt());

        when(response.getOutputStream()).thenReturn(servletOutputStream);

        // Przygotowanie danych testowych
        Category category = new Category();
        category.setName("Groceries");

        Expense expense = new Expense();
        expense.setId(1L);
        expense.setCategory(category);
        expense.setAmount(BigDecimal.valueOf(50.0));
        expense.setDate(LocalDateTime.now());
        expense.setDescription("Test expense");
        expense.setCurrency("USD");

        List<Expense> expenses = List.of(expense);

        // Testowana metoda
        exportService.exportExpensesToExcel(expenses, response);

        // Weryfikacja odpowiedzi HTTP
        verify(response).setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        verify(response).setHeader("Content-Disposition", "attachment; filename=expenses.xlsx");

        // Weryfikacja zawartości Excela
        Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(outputStream.toByteArray()));
        assertEquals(1, workbook.getNumberOfSheets());
        assertEquals("Expenses", workbook.getSheetName(0));

        var sheet = workbook.getSheetAt(0);
        assertEquals("ID", sheet.getRow(0).getCell(0).getStringCellValue());
        assertEquals("Groceries", sheet.getRow(1).getCell(1).getStringCellValue());
        assertEquals(50.0, sheet.getRow(1).getCell(2).getNumericCellValue());

        workbook.close();
    }
}
