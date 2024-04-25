package com.finance.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ExpenseComparison {

    private BigDecimal startPeriodExpenses;
    private BigDecimal endPeriodExpenses;
}
