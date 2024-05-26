package com.finance.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
public class ExpenseDto {

    private BigDecimal amount;
    private LocalDateTime date;
    private String description;
    private Long categoryId;
}
