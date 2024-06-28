package com.wallway.transactionservice.core.exceptions;

public class AccountEntityNotFoundException extends RuntimeException {
    public AccountEntityNotFoundException(final String message) {
        super(message);
    }
}
