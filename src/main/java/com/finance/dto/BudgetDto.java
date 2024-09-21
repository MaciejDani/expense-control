package com.finance.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BudgetDto {

    private int year;
    private int month;
    private BigDecimal initialAmount;
    private BigDecimal amount;
}
