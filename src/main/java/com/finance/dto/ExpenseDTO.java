package com.finance.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
public class ExpenseDTO {

    private BigDecimal amount;
    private LocalDateTime date;
    private String description;
    private Long categoryId;
}
