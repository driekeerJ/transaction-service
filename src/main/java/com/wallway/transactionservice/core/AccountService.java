package com.wallway.transactionservice.core;

import static com.wallway.transactionservice.core.mapper.AccountMapper.toDomainModel;
import static com.wallway.transactionservice.core.mapper.AccountMapper.toEntityModel;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.wallway.transactionservice.core.exceptions.AccountEntityNotFoundException;
import com.wallway.transactionservice.domain.Account;
import com.wallway.transactionservice.repo.AccountRepository;
import com.wallway.transactionservice.repo.entity.AccountEntity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public Account getAccount(final UUID accountId) {
        final AccountEntity entity = accountRepository.getAccountEntityByNumber(accountId);
        if (entity == null) {
            throw new AccountEntityNotFoundException(String.format("The account for %s was not found", accountId));
        }
        return toDomainModel(entity);
    }

    public UUID saveAccount(final Account account) {
        final AccountEntity entity = toEntityModel(account);
        final AccountEntity savedEntity = accountRepository.save(entity);
        return savedEntity.getNumber();
    }
}
