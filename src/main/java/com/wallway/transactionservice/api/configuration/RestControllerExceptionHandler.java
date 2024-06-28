package com.wallway.transactionservice.api.configuration;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.wallway.transactionservice.core.exceptions.AccountEntityNotFoundException;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class RestControllerExceptionHandler {

    @ExceptionHandler(AccountEntityNotFoundException.class)
    public String handleEntityNotFoundException(final AccountEntityNotFoundException ex, final Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        log.warn(ex.getMessage());
        return "error";
    }
}
