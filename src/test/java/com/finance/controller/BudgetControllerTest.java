package com.finance.controller;

import com.finance.dto.BudgetDto;
import com.finance.exception.UserNotFoundException;
import com.finance.model.Budget;
import com.finance.security.UserPrincipal;
import com.finance.service.BudgetService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BudgetControllerTest {

    @Mock
    private BudgetService budgetService;

    @InjectMocks
    private BudgetController budgetController;

    private UserPrincipal userPrincipal;
    private BudgetDto budgetDto;
    private Budget budget;

    @BeforeEach
    public void setUp() {
        userPrincipal = new UserPrincipal(1L, "username", "password", List.of());
        budgetDto = new BudgetDto();
        budget = new Budget();
    }

    @Test
    public void testSetMonthlyBudget() {
        when(budgetService.setMonthlyBudget(any(BudgetDto.class), any(UserPrincipal.class))).thenReturn(budget);

        ResponseEntity<Budget> response = budgetController.setMonthlyBudget(budgetDto, userPrincipal);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(budget, response.getBody());

        verify(budgetService).setMonthlyBudget(any(BudgetDto.class), any(UserPrincipal.class));
    }

    @Test
    public void testSetMonthlyBudget_UserNotFound() {
        when(budgetService.setMonthlyBudget(any(BudgetDto.class), any(UserPrincipal.class)))
                .thenThrow(new UserNotFoundException());

        assertThrows(UserNotFoundException.class, () -> {
            budgetController.setMonthlyBudget(budgetDto, userPrincipal);
        });

        verify(budgetService).setMonthlyBudget(any(BudgetDto.class), any(UserPrincipal.class));
    }

    @Test
    public void testGetMonthlyBudget_Found() {
        when(budgetService.getMonthlyBudget(anyInt(), anyInt(), any(UserPrincipal.class)))
                .thenReturn(Optional.of(budget));

        ResponseEntity<Budget> response = budgetController.getMonthlyBudget(2024, 9, userPrincipal);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(budget, response.getBody());

        verify(budgetService).getMonthlyBudget(anyInt(), anyInt(), any(UserPrincipal.class));
    }

    @Test
    public void testGetMonthlyBudget_NotFound() {
        when(budgetService.getMonthlyBudget(anyInt(), anyInt(), any(UserPrincipal.class)))
                .thenReturn(Optional.empty());

        ResponseEntity<Budget> response = budgetController.getMonthlyBudget(2024, 9, userPrincipal);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        verify(budgetService).getMonthlyBudget(anyInt(), anyInt(), any(UserPrincipal.class));
    }

}
