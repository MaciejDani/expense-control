package com.finance.controller;

import com.finance.dto.YearMonthDto;
import com.finance.model.ExpenseComparison;
import com.finance.model.MonthlyExpense;
import com.finance.model.MonthlySummary;
import com.finance.security.UserPrincipal;
import com.finance.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;


    @GetMapping("/monthly-summary")
    public ResponseEntity<MonthlySummary> getMonthlySummary(@RequestParam int year, @RequestParam int month,
                                                            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        MonthlySummary monthlySummary = reportService.getMonthlySummary(year, month, userPrincipal);

        return ResponseEntity.ok(monthlySummary);
    }

    @GetMapping("/total-expenses")
    public ResponseEntity<BigDecimal> getTotalExpensesForMonth(@RequestParam int year, @RequestParam int month,
                                                               @AuthenticationPrincipal UserPrincipal userPrincipal) {
        BigDecimal totalExpenses = reportService.getTotalExpensesForMonth(year, month, userPrincipal);
        return ResponseEntity.ok(totalExpenses);
    }

    @GetMapping("/compare-expenses")
    public ResponseEntity<ExpenseComparison> compareExpenses(@RequestParam int startYear, @RequestParam int startMonth,
                                                             @RequestParam int endYear, @RequestParam int endMonth,
                                                             @AuthenticationPrincipal UserPrincipal userPrincipal) {
        ExpenseComparison expenseComparison = reportService.compareExpenses(startYear, startMonth, endYear, endMonth, userPrincipal);

        return ResponseEntity.ok(expenseComparison);
    }

    @PostMapping("/compare-multiple-expenses")
    public ResponseEntity<List<MonthlyExpense>> compareMultipleExpenses (@RequestBody List<YearMonthDto> yearMontList,
                                                                         @AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<MonthlyExpense> monthlyExpenses = reportService.getExpensesForMultipleMonths(yearMontList, userPrincipal);

        return ResponseEntity.ok(monthlyExpenses);
    }
}
