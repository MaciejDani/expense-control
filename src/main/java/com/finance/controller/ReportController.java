package com.finance.controller;

import com.finance.model.MonthlySummary;
import com.finance.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;


    @GetMapping("/monthly-summary")
    public ResponseEntity<MonthlySummary> getMonthlySummary(@RequestParam int year, @RequestParam int month) {
        MonthlySummary monthlySummary = reportService.getMonthlySummary(year, month);
        return ResponseEntity.ok(monthlySummary);
    }

    @GetMapping("/total-expenses")
    public ResponseEntity<BigDecimal> getTotalExpensesForMonth(@RequestParam int year, @RequestParam int month) {
        BigDecimal totalExpenses = reportService.getTotalExpensesForMonth(year, month);
        return ResponseEntity.ok(totalExpenses);
    }
}
