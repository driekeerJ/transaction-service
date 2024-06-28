package com.wallway.transactionservice.core.mapper;

import org.springframework.stereotype.Component;

import com.wallway.transactionservice.domain.Account;
import com.wallway.transactionservice.repo.entity.AccountEntity;

@Component
public class AccountMapper {
    public static Account toDomainModel(final AccountEntity entity) {
        return new Account(
                entity.getNumber(),
                entity.getName(),
                entity.getEmail(),
                entity.getAmount()
        );
    }

    public static AccountEntity toEntityModel(final Account account) {
        final AccountEntity accountEntity = new AccountEntity();
        accountEntity.setName(account.name());
        accountEntity.setEmail(account.email());
        accountEntity.setAmount(account.amount());
        return accountEntity;
    }
}
