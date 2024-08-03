package com.finance.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class CurrencyConverter {

    private static final String API_KEY = "3bdd1bbb9b79b6e003e15f79";
    private static final String EXCHANGE_RATE_API_URL = "https://v6.exchangerate-api.com/v6/{apikey}/latest/{base}";

    public BigDecimal convert(BigDecimal amount, String fromCurrency, String toCurrency) {
        if (fromCurrency.equals(toCurrency)) {
            return amount;
        }
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> response = restTemplate.getForObject(EXCHANGE_RATE_API_URL, Map.class, API_KEY, fromCurrency);
        Map<String, Object> conversionRates = (Map<String, Object>) response.get("conversion_rates");
        BigDecimal rate = new BigDecimal(conversionRates.get(toCurrency).toString());

        return amount.multiply(rate);
    }
}
