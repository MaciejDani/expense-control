package com.finance.service;

import com.finance.dto.BudgetDto;
import com.finance.exception.UserNotFoundException;
import com.finance.model.Budget;
import com.finance.model.User;
import com.finance.repository.BudgetRepository;
import com.finance.repository.UserRepository;
import com.finance.security.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BudgetServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private BudgetRepository budgetRepository;
    @InjectMocks
    private BudgetService budgetService;

    private UserPrincipal userPrincipal;
    private User user;
    private BudgetDto budgetDto;
    private Budget budget;

    @BeforeEach
    public void setUp() {
        userPrincipal = new UserPrincipal(1L, "username", "password", List.of());
        user = new User();
        user.setId(1L);

        budgetDto = new BudgetDto();
        budgetDto.setYear(2024);
        budgetDto.setMonth(9);
        budgetDto.setAmount(BigDecimal.valueOf(1000));

        budget = new Budget();
        budget.setYear(2024);
        budget.setMonth(9);
        budget.setAmount(BigDecimal.valueOf(1000));
    }

    @Test
    public void testSetMonthlyBudget_NewBudget() {
        when(userRepository.findById(userPrincipal.getId())).thenReturn(Optional.of(user));
        when(budgetRepository.findByUserAndYearAndMonth(user, budgetDto.getYear(), budgetDto.getMonth())).thenReturn(Optional.empty());
        when(budgetRepository.save(any(Budget.class))).thenReturn(budget);

        Budget savedBudget = budgetService.setMonthlyBudget(budgetDto, userPrincipal);

        assertNotNull(savedBudget);
        assertEquals(budgetDto.getAmount(), savedBudget.getAmount());
        verify(userRepository).findById(userPrincipal.getId());
        verify(budgetRepository).findByUserAndYearAndMonth(user, budgetDto.getYear(), budgetDto.getMonth());
        verify(budgetRepository).save(any(Budget.class));
    }

    @Test
    public void testSetMonthlyBudget_UpdateExistingBudget() {
        when(userRepository.findById(userPrincipal.getId())).thenReturn(Optional.of(user));
        when(budgetRepository.findByUserAndYearAndMonth(user, budgetDto.getYear(), budgetDto.getMonth())).thenReturn(Optional.of(budget));
        when(budgetRepository.save(any(Budget.class))).thenReturn(budget);

        Budget updatedBudget = budgetService.setMonthlyBudget(budgetDto, userPrincipal);

        assertNotNull(updatedBudget);
        assertEquals(budgetDto.getAmount(), updatedBudget.getAmount());
        verify(userRepository).findById(userPrincipal.getId());
        verify(budgetRepository).findByUserAndYearAndMonth(user, budgetDto.getYear(), budgetDto.getMonth());
        verify(budgetRepository).save(budget);
    }

    @Test
    public void testSetMonthlyBudget_UserNotFound() {
        when(userRepository.findById(userPrincipal.getId())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            budgetService.setMonthlyBudget(budgetDto, userPrincipal);
        });

        verify(userRepository).findById(userPrincipal.getId());
        verify(budgetRepository, never()).save(any(Budget.class));
    }

    @Test
    public void testGetMonthlyBudget_Found() {
        when(userRepository.findById(userPrincipal.getId())).thenReturn(Optional.of(user));
        when(budgetRepository.findByUserAndYearAndMonth(user, budgetDto.getYear(), budgetDto.getMonth())).thenReturn(Optional.of(budget));

        Optional<Budget> foundBudget = budgetService.getMonthlyBudget(budgetDto.getYear(), budgetDto.getMonth(), userPrincipal);

        assertTrue(foundBudget.isPresent());
        assertEquals(budgetDto.getAmount(), foundBudget.get().getAmount());
        verify(userRepository).findById(userPrincipal.getId());
        verify(budgetRepository).findByUserAndYearAndMonth(user, budgetDto.getYear(), budgetDto.getMonth());
    }

    @Test
    public void testGetMonthlyBudget_NotFound() {
        when(userRepository.findById(userPrincipal.getId())).thenReturn(Optional.of(user));
        when(budgetRepository.findByUserAndYearAndMonth(user, budgetDto.getYear(), budgetDto.getMonth())).thenReturn(Optional.empty());

        Optional<Budget> foundBudget = budgetService.getMonthlyBudget(budgetDto.getYear(), budgetDto.getMonth(), userPrincipal);

        assertFalse(foundBudget.isPresent());
        verify(userRepository).findById(userPrincipal.getId());
        verify(budgetRepository).findByUserAndYearAndMonth(user, budgetDto.getYear(), budgetDto.getMonth());
    }

    @Test
    public void testGetMonthlyBudget_UserNotFound() {
        when(userRepository.findById(userPrincipal.getId())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            budgetService.getMonthlyBudget(budgetDto.getYear(), budgetDto.getMonth(), userPrincipal);
        });

        verify(userRepository).findById(userPrincipal.getId());
        verify(budgetRepository, never()).findByUserAndYearAndMonth(any(User.class), anyInt(), anyInt());
    }
}