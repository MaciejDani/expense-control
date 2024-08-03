package com.finance.mapper;

import com.finance.dto.ExpenseDto;
import com.finance.model.Category;
import com.finance.model.Expense;
import com.finance.model.User;

public class ExpenseMapper {

    public static ExpenseDto toDTO(Expense expense) {
        ExpenseDto dto = new ExpenseDto();
        dto.setAmount(expense.getAmount());
        dto.setDate(expense.getDate());
        dto.setDescription(expense.getDescription());
        dto.setCategoryId(expense.getCategory().getId());
        dto.setUserId(expense.getUser().getId());
        dto.setCurrency(expense.getCurrency());
        return dto;
    }

    public static Expense fromDTO(ExpenseDto dto, Category category, User user) {
        Expense expense = new Expense();
        expense.setAmount(dto.getAmount());
        expense.setDate(dto.getDate());
        expense.setDescription(dto.getDescription());
        expense.setCategory(category);
        expense.setUser(user);
        expense.setCurrency(dto.getCurrency());
        return expense;
}

}
