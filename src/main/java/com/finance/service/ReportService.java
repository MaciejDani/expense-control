package com.finance.service;

import com.finance.dto.YearMonthDto;
import com.finance.exception.UserNotFoundException;
import com.finance.model.*;
import com.finance.repository.ExpenseRepository;
import com.finance.repository.UserRepository;
import com.finance.security.UserPrincipal;
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

    @Autowired
    private UserRepository userRepository;

    public MonthlySummary getMonthlySummary(int year, int month, UserPrincipal userPrincipal) {
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(UserNotFoundException::new);

        List<Expense> expenses = expenseRepository.findByYearAndMonthAndUser(year, month, user);
        Map<String, BigDecimal> categorySum = expenses.stream()
                .collect(Collectors.groupingBy(expense -> expense.getCategory().getName(),
                        Collectors.reducing(BigDecimal.ZERO, Expense::getAmount, BigDecimal::add)));

        return new MonthlySummary(year, month, categorySum);
    }

    public BigDecimal getTotalExpensesForMonth(int year, int month, UserPrincipal userPrincipal) {
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(UserNotFoundException::new);

        return expenseRepository.findTotalExpensesByYearAndMonthAndUser(year, month, user);
    }

    public ExpenseComparison compareExpenses(int startYear, int startMonth, int endYear, int endMonth, UserPrincipal userPrincipal) {
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(UserNotFoundException::new);

        BigDecimal startPeriodExpenses = expenseRepository.findTotalExpensesByYearAndMonthAndUser(startYear, startMonth, user);
        BigDecimal endPeriodExpenses = expenseRepository.findTotalExpensesByYearAndMonthAndUser(endYear, endMonth, user);

        return new ExpenseComparison(startPeriodExpenses, endPeriodExpenses);
    }

    public List<MonthlyExpense> getExpensesForMultipleMonths(List<YearMonthDto> yearMonthList, UserPrincipal userPrincipal) {
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(UserNotFoundException::new);

       return yearMonthList.stream()
                .map(ym -> {
                    BigDecimal totalExpenses = expenseRepository.findTotalExpensesByYearAndMonthAndUser(ym.getYear(), ym.getMonth(), user);
                    return new MonthlyExpense(ym.getYear(), ym.getMonth(), totalExpenses);
                })
               .collect(Collectors.toList());
    }
}
