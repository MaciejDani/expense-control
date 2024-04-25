package com.finance.service;

import com.finance.dto.YearMonthDto;
import com.finance.model.Expense;
import com.finance.model.ExpenseComparison;
import com.finance.model.MonthlyExpense;
import com.finance.model.MonthlySummary;
import com.finance.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private ExpenseRepository expenseRepository;

    public MonthlySummary getMonthlySummary(int year, int month) {
        List<Expense> expenses = expenseRepository.findByYearAndMonth(year, month);
        Map<String, BigDecimal> categorySum = expenses.stream()
                .collect(Collectors.groupingBy(expense -> expense.getCategory().getName(),
                        Collectors.reducing(BigDecimal.ZERO, Expense::getAmount, BigDecimal::add)));

        return new MonthlySummary(year, month, categorySum);
    }

    public BigDecimal getTotalExpensesForMonth(int year, int month) {
        return expenseRepository.findTotalExpensesByYearAndMonth(year, month);
    }

    public ExpenseComparison compareExpenses(int startYear, int startMonth, int endYear, int endMonth) {
        BigDecimal startPeriodExpenses = expenseRepository.findTotalExpensesByYearAndMonth(startYear, startMonth);
        BigDecimal endPeriodExpenses = expenseRepository.findTotalExpensesByYearAndMonth(endYear, endMonth);

        return new ExpenseComparison(startPeriodExpenses, endPeriodExpenses);
    }

    public List<MonthlyExpense> getExpensesForMultipleMonths(List<YearMonthDto> yearMonthList) {
       return yearMonthList.stream()
                .map(ym -> {
                    BigDecimal totalExpenses = expenseRepository.findTotalExpensesByYearAndMonth(ym.getYear(), ym.getMonth());
                    return new MonthlyExpense(ym.getYear(), ym.getMonth(), totalExpenses);
                })
               .collect(Collectors.toList());
    }
}
