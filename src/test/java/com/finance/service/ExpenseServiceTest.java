package com.finance.service;

import com.finance.dto.ExpenseDto;
import com.finance.exception.CategoryNotFoundException;
import com.finance.exception.ExpenseNotFoundException;
import com.finance.exception.UserNotFoundException;
import com.finance.mapper.ExpenseMapper;
import com.finance.model.Category;
import com.finance.model.Expense;
import com.finance.model.User;
import com.finance.repository.CategoryRepository;
import com.finance.repository.ExpenseRepository;
import com.finance.repository.UserRepository;
import com.finance.security.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExpenseServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private ExpenseService expenseService;

    private UserPrincipal userPrincipal;
    private User user;
    private Category category;
    private ExpenseDto expenseDto;
    private Expense expense;

    @BeforeEach
    public void setUp() {
        userPrincipal = new UserPrincipal(1L, "username", "password", List.of());
        user = new User();
        user.setId(1L);
        category = new Category();
        category.setId(1L);
        expenseDto = new ExpenseDto();
        expenseDto.setCategoryId(1L);
        expense = new Expense();
    }

    @Test
    public void testSaveExpense() {
        when(userRepository.findById(userPrincipal.getId())).thenReturn(Optional.of(user));
        when(categoryRepository.findByIdAndUser(expenseDto.getCategoryId(), user)).thenReturn(Optional.of(category));
        when(expenseRepository.save(any(Expense.class))).thenReturn(expense);

        Expense savedExpense = expenseService.saveExpense(expenseDto, userPrincipal);

        assertNotNull(savedExpense);
        verify(userRepository).findById(userPrincipal.getId());
        verify(categoryRepository).findByIdAndUser(expenseDto.getCategoryId(), user);
        verify(expenseRepository).save(any(Expense.class));
    }

    @Test
    public void testSaveExpense_UserNotFound() {
        when(userRepository.findById(userPrincipal.getId())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            expenseService.saveExpense(expenseDto, userPrincipal);
        });

        verify(userRepository).findById(userPrincipal.getId());
    }

    @Test
    public void testSaveExpense_CategoryNotFound() {
        when(userRepository.findById(userPrincipal.getId())).thenReturn(Optional.of(user));
        when(categoryRepository.findByIdAndUser(expenseDto.getCategoryId(), user)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> {
            expenseService.saveExpense(expenseDto, userPrincipal);
        });

        verify(userRepository).findById(userPrincipal.getId());
        verify(categoryRepository).findByIdAndUser(expenseDto.getCategoryId(), user);
    }

    @Test
    public void testGetAllExpenses() {
        when(userRepository.findById(userPrincipal.getId())).thenReturn(Optional.of(user));
        when(expenseRepository.findByUser(user)).thenReturn(List.of(expense));

        List<Expense> expenses = expenseService.getAllExpenses(userPrincipal);

        assertNotNull(expenses);
        assertFalse(expenses.isEmpty());
        verify(userRepository).findById(userPrincipal.getId());
        verify(expenseRepository).findByUser(user);
    }

    @Test
    public void testGetAllExpenses_UserNotFound() {
        when(userRepository.findById(userPrincipal.getId())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            expenseService.getAllExpenses(userPrincipal);
        });

        verify(userRepository).findById(userPrincipal.getId());
    }

    @Test
    public void testGetExpenseById() {
        when(userRepository.findById(userPrincipal.getId())).thenReturn(Optional.of(user));
        when(expenseRepository.findByIdAndUser(1L, user)).thenReturn(Optional.of(expense));

        Optional<Expense> foundExpense = expenseService.getExpenseById(1L, userPrincipal);

        assertTrue(foundExpense.isPresent());
        verify(userRepository).findById(userPrincipal.getId());
        verify(expenseRepository).findByIdAndUser(1L, user);
    }

    @Test
    public void testGetExpenseById_UserNotFound() {
        when(userRepository.findById(userPrincipal.getId())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            expenseService.getExpenseById(1L, userPrincipal);
        });

        verify(userRepository).findById(userPrincipal.getId());
    }

    @Test
    public void testDeleteExpense() {
        when(userRepository.findById(userPrincipal.getId())).thenReturn(Optional.of(user));
        when(expenseRepository.findByIdAndUser(1L, user)).thenReturn(Optional.of(expense));

        expenseService.deleteExpense(1L, userPrincipal);

        verify(expenseRepository).deleteById(1L);
    }

    @Test
    public void testDeleteExpense_UserNotFound() {
        when(userRepository.findById(userPrincipal.getId())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            expenseService.deleteExpense(1L, userPrincipal);
        });

        verify(userRepository).findById(userPrincipal.getId());
        verify(expenseRepository, never()).deleteById(anyLong());
    }

    @Test
    public void testDeleteExpense_ExpenseNotFound() {
        when(userRepository.findById(userPrincipal.getId())).thenReturn(Optional.of(user));
        when(expenseRepository.findByIdAndUser(1L, user)).thenReturn(Optional.empty());

        assertThrows(ExpenseNotFoundException.class, () -> {
            expenseService.deleteExpense(1L, userPrincipal);
        });

        verify(expenseRepository, never()).deleteById(anyLong());
    }
}
