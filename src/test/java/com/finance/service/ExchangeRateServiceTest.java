package com.finance.service;

import com.finance.exception.CurrencyNotFoundException;
import com.finance.exception.InvalidResponseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class ExchangeRateServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ExchangeRateService exchangeRateService;

    private static final String API_KEY = "3bdd1bbb9b79b6e003e15f79";
    private static final String EXCHANGE_RATE_API_URL = "https://v6.exchangerate-api.com/v6/{apikey}/latest/{base}";

    @BeforeEach
    void setUp() {
        exchangeRateService = new ExchangeRateService(restTemplate);
    }

    @Test
    void testFetchExchangeRate_Valid() {
        String from = "USD";
        String to = "EUR";
        BigDecimal rate = new BigDecimal("0.85");

        Map<String, Object> response = new HashMap<>();
        Map<String, Object> rates = new HashMap<>();
        rates.put(to, rate.toString());
        response.put("conversion_rates", rates);

        when(restTemplate.getForObject(EXCHANGE_RATE_API_URL, Map.class, API_KEY, from)).thenReturn(response);

        BigDecimal result = exchangeRateService.fetchExchangeRate(from, to);
        assertEquals(rate, result);
    }

    @Test
    void testFetchExchangeRate_CurrencyNotFound() {
        Map<String, Object> response = new HashMap<>();
        response.put("conversion_rates", new HashMap<>());

        when(restTemplate.getForObject(EXCHANGE_RATE_API_URL, Map.class, API_KEY, "USD")).thenReturn(response);

        assertThrows(CurrencyNotFoundException.class, () ->
                exchangeRateService.fetchExchangeRate("USD", "XYZ"));
    }

    @Test
    void testFetchExchangeRate_InvalidResponse_Null() {
        when(restTemplate.getForObject(EXCHANGE_RATE_API_URL, Map.class, API_KEY, "USD")).thenReturn(null);
        assertThrows(InvalidResponseException.class, () ->
                exchangeRateService.fetchExchangeRate("USD", "EUR"));
    }

    @Test
    void testFetchExchangeRate_InvalidResponse_NoRatesKey() {
        Map<String, Object> response = new HashMap<>();
        when(restTemplate.getForObject(EXCHANGE_RATE_API_URL, Map.class, API_KEY, "USD")).thenReturn(response);
        assertThrows(InvalidResponseException.class, () ->
                exchangeRateService.fetchExchangeRate("USD", "EUR"));
    }

    @Test
    void testFetchExchangeRate_InvalidRateFormat() {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> rates = new HashMap<>();
        rates.put("EUR", "not_a_number");
        response.put("conversion_rates", rates);

        when(restTemplate.getForObject(EXCHANGE_RATE_API_URL, Map.class, API_KEY, "USD")).thenReturn(response);

        assertThrows(InvalidResponseException.class, () ->
                exchangeRateService.fetchExchangeRate("USD", "EUR"));
    }
}
