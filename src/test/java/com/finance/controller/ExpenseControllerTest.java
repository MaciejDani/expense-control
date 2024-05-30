package com.finance.controller;

import com.finance.dto.ExpenseDto;
import com.finance.exception.ExpenseNotFoundException;
import com.finance.model.Expense;
import com.finance.security.UserPrincipal;
import com.finance.service.ExpenseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class ExpenseControllerTest {

    @Mock
    private ExpenseService expenseService;

    @InjectMocks
    private ExpenseController expenseController;

    private UserPrincipal userPrincipal;
    private ExpenseDto expenseDto;
    private Expense expense;

    @BeforeEach
    public void setUp() {
        userPrincipal = new UserPrincipal(1L, "username", "password", List.of());
        expenseDto = new ExpenseDto();
        expense = new Expense();
    }

    @Test
    public void testAddExpense() {
        when(expenseService.saveExpense(any(ExpenseDto.class), any(UserPrincipal.class))).thenReturn(expense);

        ResponseEntity<Expense> response = expenseController.addExpense(expenseDto, userPrincipal);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expense, response.getBody());
        verify(expenseService).saveExpense(any(ExpenseDto.class), any(UserPrincipal.class));
    }

    @Test
    public void testGetAllExpenses() {
        when(expenseService.getAllExpenses(any(UserPrincipal.class))).thenReturn(List.of(expense));

        ResponseEntity<List<Expense>> response = expenseController.getAllExpenses(userPrincipal);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
        verify(expenseService).getAllExpenses(any(UserPrincipal.class));
    }

    @Test
    public void testGetExpenseById_Found() {
        when(expenseService.getExpenseById(anyLong(), any(UserPrincipal.class))).thenReturn(Optional.of(expense));

        ResponseEntity<Expense> response = expenseController.getExpenseById(1L, userPrincipal);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expense, response.getBody());
        verify(expenseService).getExpenseById(anyLong(), any(UserPrincipal.class));
    }

    @Test
    public void testGetExpenseById_NotFound() {
        when(expenseService.getExpenseById(anyLong(), any(UserPrincipal.class))).thenReturn(Optional.empty());

        ResponseEntity<Expense> response = expenseController.getExpenseById(1L, userPrincipal);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(expenseService).getExpenseById(anyLong(), any(UserPrincipal.class));
    }

    @Test
    public void testDeleteExpense() {
        doNothing().when(expenseService).deleteExpense(anyLong(), any(UserPrincipal.class));

        ResponseEntity<Void> response = expenseController.deleteExpense(1L, userPrincipal);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(expenseService).deleteExpense(anyLong(), any(UserPrincipal.class));
    }

    @Test
    public void testDeleteExpense_NotFound() {
        doThrow(new ExpenseNotFoundException()).when(expenseService).deleteExpense(anyLong(), any(UserPrincipal.class));

        assertThrows(ExpenseNotFoundException.class, () -> {
            expenseController.deleteExpense(1L, userPrincipal);
        });

        verify(expenseService).deleteExpense(anyLong(), any(UserPrincipal.class));
    }

}
