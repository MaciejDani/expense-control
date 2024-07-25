package com.finance.service;

import com.finance.dto.ExpenseDto;
import com.finance.exception.BudgetNotFoundException;
import com.finance.exception.CategoryNotFoundException;
import com.finance.exception.ExpenseNotFoundException;
import com.finance.exception.UserNotFoundException;
import com.finance.mapper.ExpenseMapper;
import com.finance.model.Budget;
import com.finance.model.Category;
import com.finance.model.Expense;
import com.finance.model.User;
import com.finance.repository.BudgetRepository;
import com.finance.repository.CategoryRepository;
import com.finance.repository.ExpenseRepository;
import com.finance.repository.UserRepository;
import com.finance.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BudgetRepository budgetRepository;

    public Expense saveExpense(ExpenseDto expenseDto, UserPrincipal userPrincipal) {
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(UserNotFoundException::new);

        Category category = categoryRepository.findByIdAndUser(expenseDto.getCategoryId(), user)
                .orElseThrow(CategoryNotFoundException::new);

        Expense expense = ExpenseMapper.fromDTO(expenseDto, category);
        expense.setUser(user);

        updateBudgetAfterExpense(user, expense.getDate(), expense.getAmount().negate());

        return expenseRepository.save(expense);
    }

    public List<Expense> getAllExpenses(UserPrincipal userPrincipal) {
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(UserNotFoundException::new);

        return expenseRepository.findByUser(user);
    }

    public Optional<Expense> getExpenseById(Long id, UserPrincipal userPrincipal) {
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(UserNotFoundException::new);

        return expenseRepository.findByIdAndUser(id, user);
    }

    public void deleteExpense(Long id, UserPrincipal userPrincipal) {
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(UserNotFoundException::new);

        Optional<Expense> optionalExpense = expenseRepository.findByIdAndUser(id, user);
        if (optionalExpense.isPresent()) {
            Expense expense = optionalExpense.get();

            updateBudgetAfterExpense(user, expense.getDate(), expense.getAmount());

            expenseRepository.deleteById(id);
        } else {
            throw new ExpenseNotFoundException();
        }
    }

    private void updateBudgetAfterExpense(User user, LocalDateTime date, BigDecimal amountChange) {
        int year = date.getYear();
        int month = date.getMonthValue();

        Budget budget = budgetRepository.findByUserAndYearAndMonth(user, year, month)
                .orElseThrow(BudgetNotFoundException::new);

        budget.setAmount(budget.getAmount().add(amountChange));
        budgetRepository.save(budget);
    }
}
