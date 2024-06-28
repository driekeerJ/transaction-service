package com.wallway.transactionservice.domain;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Builder;

@Builder
public record Account(UUID number, String name, String email, BigDecimal amount) {
}
