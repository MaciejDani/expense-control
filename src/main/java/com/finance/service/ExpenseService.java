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
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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

    public Expense saveExpense(ExpenseDto expenseDto, UserPrincipal userPrincipal) {
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(UserNotFoundException::new);

        Category category = categoryRepository.findByIdAndUser(expenseDto.getCategoryId(), user)
                .orElseThrow(CategoryNotFoundException::new);

        Expense expense = ExpenseMapper.fromDTO(expenseDto, category);
        expense.setUser(user);

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

        Optional<Expense> expense = expenseRepository.findByIdAndUser(id, user);
        if (expense.isPresent()) {
            expenseRepository.deleteById(id);
        } else {
            throw new ExpenseNotFoundException();
        }
    }
}
