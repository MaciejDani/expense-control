package com.finance.mapper;

import com.finance.dto.BudgetDto;
import com.finance.model.Budget;

public class BudgetMapper {

    public static BudgetDto toDto(Budget budget) {
        BudgetDto dto = new BudgetDto();
        dto.setAmount(budget.getAmount());
        dto.setYear(budget.getYear());
        dto.setInitialAmount(budget.getInitialAmount());
        dto.setMonth(budget.getMonth());
        return dto;
    }

    public static Budget fromDto(BudgetDto budgetDto) {
        Budget budget = new Budget();
        budget.setAmount(budgetDto.getAmount());
        budget.setYear(budgetDto.getYear());
        budget.setInitialAmount(budgetDto.getInitialAmount());
        budget.setMonth(budgetDto.getMonth());
        return budget;
    }
}
