package com.wallway.transactionservice;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.wallway.transactionservice.repo.AccountRepository;
import com.wallway.transactionservice.repo.entity.AccountEntity;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ApplicationITTestHelper {

    protected static final String NAME = "John Doe";
    protected static final BigDecimal AMOUNT = BigDecimal.valueOf(45.62d);
    protected static final String MAIL = "bogus@email.com";
    private final AccountRepository accountRepository;

    public UUID saveAccount() {
        final AccountEntity entity = new AccountEntity();
        entity.setName(NAME);
        entity.setAmount(AMOUNT);
        entity.setEmail(MAIL);
        return accountRepository.save(entity).getNumber();
    }
}
