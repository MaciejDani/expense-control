package com.finance.exception;

public class InvalidResponseException extends RuntimeException {
    public InvalidResponseException() {
        super("Invalid response from exchange rate API");
    }
}
