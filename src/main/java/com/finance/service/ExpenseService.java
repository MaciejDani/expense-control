package com.finance.service;

import com.finance.dto.ExpenseDTO;
import com.finance.mapper.ExpenseMapper;
import com.finance.model.Category;
import com.finance.model.Expense;
import com.finance.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private CategoryService categoryService;

    public Expense saveExpense(ExpenseDTO expenseDTO) {
        Category category = categoryService.getCategoryById(expenseDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        Expense expense = ExpenseMapper.fromDTO(expenseDTO, category);

        return expenseRepository.save(expense);
    }

    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    public Expense getExpenseById(Long id) {
        return expenseRepository.findById(id).orElseThrow(() -> new RuntimeException("Expense not found"));
    }

    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }
}
