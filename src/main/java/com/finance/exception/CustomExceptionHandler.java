package com.finance.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException userNotFoundException) {
        return new ResponseEntity<>(userNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<String> handleCategoryNotFoundException(CategoryNotFoundException categoryNotFoundException) {
        return new ResponseEntity<>(categoryNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ExpenseNotFoundException.class)
    public ResponseEntity<String> handleExpenseNotFoundException(ExpenseNotFoundException expenseNotFoundException) {
        return new ResponseEntity<>(expenseNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailAlreadyInUseException.class)
    public ResponseEntity<String> handleEmailAlreadyInUseException(EmailAlreadyInUseException emailAlreadyInUseException) {
        return new ResponseEntity<>(emailAlreadyInUseException.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameAlreadyTakenException.class)
    public ResponseEntity<String> handleUsernameAlreadyTakenException(UsernameAlreadyTakenException usernameAlreadyTakenException) {
        return new ResponseEntity<>(usernameAlreadyTakenException.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidLoginException.class)
    public ResponseEntity<String> handleInvalidLoginException(InvalidLoginException invalidLoginException) {
        return new ResponseEntity<>(invalidLoginException.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BudgetNotFoundException.class)
    public ResponseEntity<String> handleBudgetNotFoundException(BudgetNotFoundException budgetNotFoundException) {
        return new ResponseEntity<>(budgetNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidResponseException.class)
    public ResponseEntity<String> handleInvalidResponseException(InvalidResponseException invalidResponseException) {
        return new ResponseEntity<>(invalidResponseException.getMessage(), HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(CurrencyNotFoundException.class)
    public ResponseEntity<String> handleCurrencyNotFoundException (CurrencyNotFoundException currencyNotFoundException) {
        return new ResponseEntity<>(currencyNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
    }

}
