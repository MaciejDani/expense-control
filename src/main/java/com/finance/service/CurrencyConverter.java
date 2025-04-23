package com.finance.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CurrencyConverter {

    private final ExchangeRateService exchangeRateService;

    public CurrencyConverter(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    public BigDecimal convert(BigDecimal amount, String fromCurrency, String toCurrency) {
        if (fromCurrency.equals(toCurrency)) {
            return amount;
        }

        BigDecimal rate = exchangeRateService.fetchExchangeRate(fromCurrency, toCurrency);
        return amount.multiply(rate);
    }
}
