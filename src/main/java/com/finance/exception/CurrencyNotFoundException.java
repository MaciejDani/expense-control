package com.finance.exception;


public class CurrencyNotFoundException extends RuntimeException {
    public CurrencyNotFoundException() {
        super("Currency not found in conversion rates");
    }
}
