package com.finance.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlySummary {

    private int year;
    private int month;
    private Map<String, BigDecimal> categorySum;
}
