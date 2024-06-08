package com.finance.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseComparison {

    private BigDecimal startPeriodExpenses;
    private BigDecimal endPeriodExpenses;
}
