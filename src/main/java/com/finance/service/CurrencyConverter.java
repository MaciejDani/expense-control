package com.finance.service;

import com.finance.exception.CurrencyNotFoundException;
import com.finance.exception.InvalidResponseException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class CurrencyConverter {

    private final RestTemplate restTemplate;
    private static final String API_KEY = "3bdd1bbb9b79b6e003e15f79";
    private static final String EXCHANGE_RATE_API_URL = "https://v6.exchangerate-api.com/v6/{apikey}/latest/{base}";

    public CurrencyConverter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public BigDecimal convert(BigDecimal amount, String fromCurrency, String toCurrency) {
        if (fromCurrency.equals(toCurrency)) {
            return amount;
        }
        BigDecimal rate = fetchExchangeRate(fromCurrency, toCurrency);

        return amount.multiply(rate);
    }

    private BigDecimal fetchExchangeRate(String fromCurrency, String toCurrency) {
        Map<String, Object> response = restTemplate.getForObject(EXCHANGE_RATE_API_URL, Map.class, API_KEY, fromCurrency);
        if (response == null || !response.containsKey("conversion_rates")) {
            throw new InvalidResponseException();
        }

        Map<String, Object> conversionRates = (Map<String, Object>) response.get("conversion_rates");
        if (!conversionRates.containsKey(toCurrency)) {
            throw new CurrencyNotFoundException();
        }
        try {
        return new BigDecimal(conversionRates.get(toCurrency).toString());
    } catch (NumberFormatException e) {
            throw new InvalidResponseException();
        }
        }
}
