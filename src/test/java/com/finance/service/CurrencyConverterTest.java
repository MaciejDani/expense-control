package com.finance.service;

import com.finance.exception.CurrencyNotFoundException;
import com.finance.exception.InvalidResponseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CurrencyConverterTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    CurrencyConverter currencyConverter;

    private static final String API_KEY = "3bdd1bbb9b79b6e003e15f79";
    private static final String EXCHANGE_RATE_API_URL = "https://v6.exchangerate-api.com/v6/{apikey}/latest/{base}";

    @BeforeEach
    void setUp() {
        currencyConverter = new CurrencyConverter(restTemplate);
    }

    @Test
    void testConvert_SameCurrency() {
        BigDecimal amount = new BigDecimal("100");
        BigDecimal result = currencyConverter.convert(amount, "USD", "USD");
        assertEquals(amount, result);
    }

    @Test
    void testConvert_ValidCurrencies() {
        BigDecimal amount = new BigDecimal("100");
        String fromCurrency = "USD";
        String toCurrency = "EUR";
        BigDecimal exchangeRate = new BigDecimal("0.85");

        Map<String, Object> mockResponse = new HashMap<>();
        Map<String, Object> conversionRates = new HashMap<>();
        conversionRates.put(toCurrency, exchangeRate.toString());
        mockResponse.put("conversion_rates", conversionRates);

        when(restTemplate.getForObject(EXCHANGE_RATE_API_URL, Map.class, API_KEY, fromCurrency))
                .thenReturn(mockResponse);

        BigDecimal result = currencyConverter.convert(amount, fromCurrency, toCurrency);
        assertEquals(amount.multiply(exchangeRate), result);
    }

    @Test
    void testConvert_CurrencyNotFound() {
        String fromCurrency = "USD";
        String toCurrency = "XYZ";

        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("conversion_rates", new HashMap<>());

        when(restTemplate.getForObject(EXCHANGE_RATE_API_URL, Map.class, API_KEY, fromCurrency))
                .thenReturn(mockResponse);

        assertThrows(CurrencyNotFoundException.class, () ->
                currencyConverter.convert(new BigDecimal("100"), fromCurrency, toCurrency));
    }

    @Test
    void testConvert_InvalidResponse() {
        String fromCurrency = "USD";
        when(restTemplate.getForObject(EXCHANGE_RATE_API_URL, Map.class, API_KEY, fromCurrency))
                .thenReturn(null);

        assertThrows(InvalidResponseException.class, () ->
                currencyConverter.convert(new BigDecimal("100"), fromCurrency, "EUR"));
    }

    @Test
    void testConvert_MalformedExchangeRate() {
        String fromCurrency = "USD";
        String toCurrency = "EUR";

        Map<String, Object> mockResponse = new HashMap<>();
        Map<String, Object> conversionRates = new HashMap<>();
        conversionRates.put(toCurrency, "invalid_value");
        mockResponse.put("conversion_rates", conversionRates);

        when(restTemplate.getForObject(EXCHANGE_RATE_API_URL, Map.class, API_KEY, fromCurrency))
                .thenReturn(mockResponse);

        assertThrows(InvalidResponseException.class, () ->
                currencyConverter.convert(new BigDecimal("100"), fromCurrency, toCurrency));
    }
}
