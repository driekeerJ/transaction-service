package com.wallway.transactionservice.domain;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Builder;

@Builder
public record Transaction(UUID fromAccountNumber, UUID toAccountNumber, String description, BigDecimal amount) {
}
