package com.wallway.transactionservice.core.exceptions;

public class AccountAlreadyExistsException extends RuntimeException {
    public AccountAlreadyExistsException(final String message) {
        super(message);
    }
}
