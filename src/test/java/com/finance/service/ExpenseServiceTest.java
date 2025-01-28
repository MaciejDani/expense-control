package com.finance.service;

import com.finance.dto.ExpenseDto;
import com.finance.exception.CategoryNotFoundException;
import com.finance.exception.ExpenseNotFoundException;
import com.finance.exception.UserNotFoundException;
import com.finance.model.Budget;
import com.finance.model.Category;
import com.finance.model.Expense;
import com.finance.model.User;
import com.finance.repository.BudgetRepository;
import com.finance.repository.CategoryRepository;
import com.finance.repository.ExpenseRepository;
import com.finance.repository.UserRepository;
import com.finance.security.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExpenseServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CurrencyConverter currencyConverter;

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private BudgetRepository budgetRepository;

    @InjectMocks
    private ExpenseService expenseService;

    private User user;
    private Category category;
    private ExpenseDto expenseDto;
    private Expense expense;
    private UserPrincipal userPrincipal;
    private Budget budget;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock data setup
        user = new User();
        user.setId(1L);
        user.setDefaultCurrency("USD");

        category = new Category();
        category.setId(1L);
        category.setUser(user);

        expense = new Expense();
        expense.setId(1L);
        expense.setAmount(BigDecimal.valueOf(100));
        expense.setCurrency("USD");
        expense.setDate(LocalDateTime.now());
        expense.setCategory(category);
        expense.setUser(user);

        expenseDto = new ExpenseDto();
        expenseDto.setAmount(BigDecimal.valueOf(100));
        expenseDto.setCurrency("EUR");
        expenseDto.setCategoryId(1L);
        expenseDto.setDate(LocalDateTime.now());
        expenseDto.setDescription("Test expense");

        budget = new Budget();
        budget.setAmount(BigDecimal.valueOf(1200));

        userPrincipal = mock(UserPrincipal.class);
        when(userPrincipal.getId()).thenReturn(1L);
    }

    @Test
    void testSaveExpense() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(categoryRepository.findByIdAndUser(1L, user)).thenReturn(Optional.of(category));
        when(currencyConverter.convert(BigDecimal.valueOf(100), "EUR", "USD")).thenReturn(BigDecimal.valueOf(120));
        when(expenseRepository.save(any(Expense.class))).thenAnswer(invocation -> {
            Expense savedExpense = invocation.getArgument(0);
            savedExpense.setId(1L);
            return savedExpense;
        });
        when(budgetRepository.findByUserAndYearAndMonth(any(User.class), anyInt(), anyInt())).thenReturn(Optional.of(budget));

        Expense savedExpense = expenseService.saveExpense(expenseDto, userPrincipal);

        assertNotNull(savedExpense);
        assertEquals(BigDecimal.valueOf(120), savedExpense.getAmount());
        assertEquals("USD", savedExpense.getCurrency());
        assertEquals(user, savedExpense.getUser());
        assertEquals(category, savedExpense.getCategory());

        verify(userRepository).findById(1L);
        verify(categoryRepository).findByIdAndUser(1L, user);
        verify(currencyConverter).convert(BigDecimal.valueOf(100), "EUR", "USD");
        verify(expenseRepository).save(any(Expense.class));
        verify(budgetRepository).findByUserAndYearAndMonth(any(User.class), anyInt(), anyInt());
        verify(budgetRepository).save(any(Budget.class));
    }

    @Test
    void testSaveExpense_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            expenseService.saveExpense(expenseDto, userPrincipal);
        });
        verify(userRepository).findById(1L);
    }

    @Test
    void testSaveExpense_CategoryNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(categoryRepository.findByIdAndUser(1L, user)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> {
            expenseService.saveExpense(expenseDto, userPrincipal);
        });
        verify(userRepository).findById(1L);
        verify(categoryRepository).findByIdAndUser(1L, user);
    }

    @Test
    void testGetAllExpenses() {
        when(userRepository.findById(userPrincipal.getId())).thenReturn(Optional.of(user));
        when(expenseRepository.findByUser(user)).thenReturn(List.of(expense));

        List<Expense> expenses = expenseService.getAllExpenses(userPrincipal);

        assertNotNull(expenses);
        assertFalse(expenses.isEmpty());
        assertEquals(1, expenses.size());
        assertEquals(expense, expenses.get(0));

        verify(userRepository).findById(userPrincipal.getId());
        verify(expenseRepository).findByUser(user);
    }

    @Test
    void testGetAllExpenses_UserNotFound() {
        when(userRepository.findById(userPrincipal.getId())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            expenseService.getAllExpenses(userPrincipal);
        });

        verify(userRepository).findById(userPrincipal.getId());
        verify(expenseRepository, never()).findByUser(any(User.class));
    }

    @Test
    void testGetExpenseById() {
        when(userRepository.findById(userPrincipal.getId())).thenReturn(Optional.of(user));
        when(expenseRepository.findByIdAndUser(1L, user)).thenReturn(Optional.of(expense));

        Optional<Expense> foundExpense = expenseService.getExpenseById(1L, userPrincipal);

        assertTrue(foundExpense.isPresent());
        assertEquals(expense, foundExpense.get());

        verify(userRepository).findById(userPrincipal.getId());
        verify(expenseRepository).findByIdAndUser(1L, user);
    }

    @Test
    void testGetExpenseById_UserNotFound() {
        when(userRepository.findById(userPrincipal.getId())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            expenseService.getExpenseById(1L, userPrincipal);
        });

        verify(userRepository).findById(userPrincipal.getId());
        verify(expenseRepository, never()).findByIdAndUser(anyLong(), any(User.class));
    }

    @Test
    void testDeleteExpense() {
        when(userRepository.findById(userPrincipal.getId())).thenReturn(Optional.of(user));
        when(expenseRepository.findByIdAndUser(1L, user)).thenReturn(Optional.of(expense));
        when(budgetRepository.findByUserAndYearAndMonth(any(User.class), anyInt(), anyInt())).thenReturn(Optional.of(budget));

        expenseService.deleteExpense(1L, userPrincipal);

        assertEquals(BigDecimal.valueOf(1300), budget.getAmount()); // zakładając, że budżet zwiększył się o 100
        verify(expenseRepository).deleteById(1L);
        verify(budgetRepository).findByUserAndYearAndMonth(user, expense.getDate().getYear(), expense.getDate().getMonthValue());
        verify(budgetRepository).save(budget);
    }

    @Test
    void testDeleteExpense_UserNotFound() {
        when(userRepository.findById(userPrincipal.getId())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            expenseService.deleteExpense(1L, userPrincipal);
        });

        verify(userRepository).findById(userPrincipal.getId());
        verify(expenseRepository, never()).deleteById(anyLong());
        verify(budgetRepository, never()).findByUserAndYearAndMonth(any(User.class), anyInt(), anyInt());
    }

    @Test
    void testDeleteExpense_ExpenseNotFound() {
        when(userRepository.findById(userPrincipal.getId())).thenReturn(Optional.of(user));
        when(expenseRepository.findByIdAndUser(1L, user)).thenReturn(Optional.empty());

        assertThrows(ExpenseNotFoundException.class, () -> {
            expenseService.deleteExpense(1L, userPrincipal);
        });

        verify(expenseRepository, never()).deleteById(anyLong());
        verify(budgetRepository, never()).findByUserAndYearAndMonth(any(User.class), anyInt(), anyInt());
    }
}
