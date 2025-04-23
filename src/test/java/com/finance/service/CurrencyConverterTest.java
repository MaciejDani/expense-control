package com.finance.service;

import com.finance.exception.CurrencyNotFoundException;
import com.finance.exception.InvalidResponseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class CurrencyConverterTest {

    @Mock
    private ExchangeRateService exchangeRateService;

    @InjectMocks
    private CurrencyConverter currencyConverter;

    @Test
    void testConvert_SameCurrency() {
        BigDecimal amount = new BigDecimal("100");
        BigDecimal result = currencyConverter.convert(amount, "USD", "USD");
        assertEquals(amount, result);
    }

    @Test
    void testConvert_ValidCurrencies() {
        BigDecimal amount = new BigDecimal("100");
        when(exchangeRateService.fetchExchangeRate("USD", "EUR")).thenReturn(new BigDecimal("0.85"));

        BigDecimal result = currencyConverter.convert(amount, "USD", "EUR");
        assertEquals(new BigDecimal("85.00"), result);
    }

    @Test
    void testConvert_CurrencyNotFound() {
        when(exchangeRateService.fetchExchangeRate("USD", "XYZ")).thenThrow(new CurrencyNotFoundException());
        assertThrows(CurrencyNotFoundException.class, () -> currencyConverter.convert(BigDecimal.TEN, "USD", "XYZ"));
    }

    @Test
    void testConvert_InvalidResponse() {
        when(exchangeRateService.fetchExchangeRate("USD", "EUR")).thenThrow(new InvalidResponseException());
        assertThrows(InvalidResponseException.class, () -> currencyConverter.convert(BigDecimal.TEN, "USD", "EUR"));
    }
}
