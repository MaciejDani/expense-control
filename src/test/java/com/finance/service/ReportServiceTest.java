package com.finance.service;
import com.finance.dto.YearMonthDto;
import com.finance.exception.UserNotFoundException;
import com.finance.model.*;
import com.finance.repository.ExpenseRepository;
import com.finance.repository.UserRepository;
import com.finance.security.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReportServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReportService reportService;

    private UserPrincipal userPrincipal;
    private User user;

    @BeforeEach
    public void setUp() {
        userPrincipal = new UserPrincipal(1L, "username", "password", List.of());
        user = new User();
        user.setId(1L);
    }

    @Test
    public void testGetMonthlySummary() {
        Category foodCategory = new Category(1L, "Food", "Food Expenses", user);
        List<Expense> expenses = List.of(
                new Expense(1L, foodCategory, new BigDecimal("10.00"), LocalDateTime.now(), "Lunch", user),
                new Expense(2L, foodCategory, new BigDecimal("20.00"), LocalDateTime.now(), "Dinner", user)
        );

        when(userRepository.findById(userPrincipal.getId())).thenReturn(Optional.of(user));
        when(expenseRepository.findByYearAndMonthAndUser(2023, 5, user)).thenReturn(expenses);

        MonthlySummary summary = reportService.getMonthlySummary(2023, 5, userPrincipal);

        assertNotNull(summary);
        assertEquals(1, summary.getCategorySum().size());
        assertEquals(new BigDecimal("30.00"), summary.getCategorySum().get("Food"));

        verify(userRepository).findById(userPrincipal.getId());
        verify(expenseRepository).findByYearAndMonthAndUser(2023, 5, user);
    }

    @Test
    public void testGetMonthlySummary_UserNotFound() {
        when(userRepository.findById(userPrincipal.getId())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            reportService.getMonthlySummary(2023, 5, userPrincipal);
        });

        verify(userRepository).findById(userPrincipal.getId());
    }

    @Test
    public void testGetTotalExpensesForMonth() {
        when(userRepository.findById(userPrincipal.getId())).thenReturn(Optional.of(user));
        when(expenseRepository.findTotalExpensesByYearAndMonthAndUser(2023, 5, user)).thenReturn(new BigDecimal("100.00"));

        BigDecimal totalExpenses = reportService.getTotalExpensesForMonth(2023, 5, userPrincipal);

        assertNotNull(totalExpenses);
        assertEquals(new BigDecimal("100.00"), totalExpenses);

        verify(userRepository).findById(userPrincipal.getId());
        verify(expenseRepository).findTotalExpensesByYearAndMonthAndUser(2023, 5, user);
    }

    @Test
    public void testGetTotalExpensesForMonth_UserNotFound() {
        when(userRepository.findById(userPrincipal.getId())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            reportService.getTotalExpensesForMonth(2023, 5, userPrincipal);
        });

        verify(userRepository).findById(userPrincipal.getId());
    }

    @Test
    public void testCompareExpenses() {
        when(userRepository.findById(userPrincipal.getId())).thenReturn(Optional.of(user));
        when(expenseRepository.findTotalExpensesByYearAndMonthAndUser(2023, 5, user)).thenReturn(new BigDecimal("100.00"));
        when(expenseRepository.findTotalExpensesByYearAndMonthAndUser(2023, 6, user)).thenReturn(new BigDecimal("150.00"));

        ExpenseComparison comparison = reportService.compareExpenses(2023, 5, 2023, 6, userPrincipal);

        assertNotNull(comparison);
        assertEquals(new BigDecimal("100.00"), comparison.getStartPeriodExpenses());
        assertEquals(new BigDecimal("150.00"), comparison.getEndPeriodExpenses());

        verify(userRepository).findById(userPrincipal.getId());
        verify(expenseRepository).findTotalExpensesByYearAndMonthAndUser(2023, 5, user);
        verify(expenseRepository).findTotalExpensesByYearAndMonthAndUser(2023, 6, user);
    }

    @Test
    public void testCompareExpenses_UserNotFound() {
        when(userRepository.findById(userPrincipal.getId())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            reportService.compareExpenses(2023, 5, 2023, 6, userPrincipal);
        });

        verify(userRepository).findById(userPrincipal.getId());
    }

    @Test
    public void testGetExpensesForMultipleMonths() {
        List<YearMonthDto> yearMonthList = List.of(new YearMonthDto(2023, 5), new YearMonthDto(2023, 6), new YearMonthDto(2023, 7));
        when(userRepository.findById(userPrincipal.getId())).thenReturn(Optional.of(user));
        when(expenseRepository.findTotalExpensesByYearAndMonthAndUser(2023, 5, user)).thenReturn(new BigDecimal("100.00"));
        when(expenseRepository.findTotalExpensesByYearAndMonthAndUser(2023, 6, user)).thenReturn(new BigDecimal("150.00"));
        when(expenseRepository.findTotalExpensesByYearAndMonthAndUser(2023, 7, user)).thenReturn(new BigDecimal("200.00"));

        List<MonthlyExpense> expenses = reportService.getExpensesForMultipleMonths(yearMonthList, userPrincipal);

        assertNotNull(expenses);
        assertEquals(3, expenses.size());
        assertEquals(new BigDecimal("100.00"), expenses.get(0).getTotalExpenses());
        assertEquals(new BigDecimal("150.00"), expenses.get(1).getTotalExpenses());
        assertEquals(new BigDecimal("200.00"), expenses.get(2).getTotalExpenses());

        verify(userRepository).findById(userPrincipal.getId());
        verify(expenseRepository).findTotalExpensesByYearAndMonthAndUser(2023, 5, user);
        verify(expenseRepository).findTotalExpensesByYearAndMonthAndUser(2023, 6, user);
        verify(expenseRepository).findTotalExpensesByYearAndMonthAndUser(2023, 7, user);
    }

    @Test
    public void testGetExpensesForMultipleMonths_UserNotFound() {
        List<YearMonthDto> yearMonthList = List.of(new YearMonthDto(2023, 5), new YearMonthDto(2023, 6));
        when(userRepository.findById(userPrincipal.getId())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            reportService.getExpensesForMultipleMonths(yearMonthList, userPrincipal);
        });

        verify(userRepository).findById(userPrincipal.getId());
    }




}
