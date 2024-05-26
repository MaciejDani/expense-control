package com.finance.mapper;

import com.finance.dto.ExpenseDto;
import com.finance.model.Category;
import com.finance.model.Expense;

public class ExpenseMapper {

    public static ExpenseDto toDTO(Expense expense) {
        ExpenseDto dto = new ExpenseDto();
        dto.setAmount(expense.getAmount());
        dto.setDate(expense.getDate());
        dto.setDescription(expense.getDescription());
        dto.setCategoryId(expense.getCategory().getId());
        return dto;
    }

    public static Expense fromDTO(ExpenseDto dto, Category category) {
        Expense expense = new Expense();
        expense.setAmount(dto.getAmount());
        expense.setDate(dto.getDate());
        expense.setDescription(dto.getDescription());
        expense.setCategory(category);
        return expense;
}}
