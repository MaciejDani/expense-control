package com.finance.service;

import com.finance.dto.BudgetDto;
import com.finance.exception.UserNotFoundException;
import com.finance.mapper.BudgetMapper;
import com.finance.model.Budget;
import com.finance.model.User;
import com.finance.repository.BudgetRepository;
import com.finance.repository.UserRepository;
import com.finance.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class BudgetService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    BudgetRepository budgetRepository;

    public Budget setMonthlyBudget(BudgetDto budgetDto, UserPrincipal userPrincipal) {

        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(UserNotFoundException::new);

        Budget budget = budgetRepository.findByUserAndYearAndMonth(user, budgetDto.getYear(), budgetDto.getMonth())
                .orElse(BudgetMapper.fromDto(budgetDto));

        budget.setUser(user);
        budget.setAmount(budgetDto.getAmount());
        return budgetRepository.save(budget);
    }

    public Optional<Budget> getMonthlyBudget(int year, int month, UserPrincipal userPrincipal) {
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(UserNotFoundException::new);

        return budgetRepository.findByUserAndYearAndMonth(user, year, month);
    }
}
