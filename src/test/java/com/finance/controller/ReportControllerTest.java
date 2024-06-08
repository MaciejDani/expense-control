package com.finance.controller;

import com.finance.dto.YearMonthDto;
import com.finance.model.ExpenseComparison;
import com.finance.model.MonthlyExpense;
import com.finance.model.MonthlySummary;
import com.finance.security.UserPrincipal;
import com.finance.service.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReportControllerTest {

    @Mock
    private ReportService reportService;

    @InjectMocks
    private ReportController reportController;

    private UserPrincipal userPrincipal;
    private MonthlySummary monthlySummary;
    private BigDecimal totalExpenses;
    private ExpenseComparison expenseComparison;
    private List<MonthlyExpense> monthlyExpenses;
    private List<YearMonthDto> yearMonthDtoList;

    @BeforeEach
    public void setUp() {
        userPrincipal = new UserPrincipal(1L, "username", "password", List.of());
        monthlySummary = new MonthlySummary();
        totalExpenses = new BigDecimal("100.00");
        expenseComparison = new ExpenseComparison();
        monthlyExpenses = List.of(new MonthlyExpense());
        yearMonthDtoList = List.of(new YearMonthDto());
    }

    @Test
    public void testGetMonthlySummary() {
        when(reportService.getMonthlySummary(anyInt(), anyInt(), any(UserPrincipal.class))).thenReturn(monthlySummary);

        ResponseEntity<MonthlySummary> response = reportController.getMonthlySummary(2023, 5, userPrincipal);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(monthlySummary, response.getBody());
        verify(reportService).getMonthlySummary(anyInt(), anyInt(), any(UserPrincipal.class));
    }

    @Test
    public void testGetTotalExpensesForMonth() {
        when(reportService.getTotalExpensesForMonth(anyInt(), anyInt(), any(UserPrincipal.class))).thenReturn(totalExpenses);

        ResponseEntity<BigDecimal> response = reportController.getTotalExpensesForMonth(2023, 5, userPrincipal);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(totalExpenses, response.getBody());
        verify(reportService).getTotalExpensesForMonth(anyInt(), anyInt(), any(UserPrincipal.class));
    }

    @Test
    public void testCompareExpenses() {
        when(reportService.compareExpenses(anyInt(), anyInt(), anyInt(), anyInt(), any(UserPrincipal.class))).thenReturn(expenseComparison);

        ResponseEntity<ExpenseComparison> response = reportController.compareExpenses(2023, 5, 2023, 6, userPrincipal);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expenseComparison, response.getBody());
        verify(reportService).compareExpenses(anyInt(), anyInt(), anyInt(), anyInt(), any(UserPrincipal.class));
    }

    @Test
    public void testCompareMultipleExpenses() {
        when(reportService.getExpensesForMultipleMonths(anyList(), any(UserPrincipal.class))).thenReturn(monthlyExpenses);

        ResponseEntity<List<MonthlyExpense>> response = reportController.compareMultipleExpenses(yearMonthDtoList, userPrincipal);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(monthlyExpenses, response.getBody());
        verify(reportService).getExpensesForMultipleMonths(anyList(), any(UserPrincipal.class));
    }
}
