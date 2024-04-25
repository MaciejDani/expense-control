package com.finance.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class MonthlyExpense {

    private int year;
    private int month;
    private BigDecimal totalExpenses;
}
