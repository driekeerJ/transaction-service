package com.wallway.transactionservice.core;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wallway.transactionservice.core.exceptions.AccountEntityNotFoundException;
import com.wallway.transactionservice.domain.Transaction;
import com.wallway.transactionservice.domain.TransactionResultType;
import com.wallway.transactionservice.repo.AccountRepository;
import com.wallway.transactionservice.repo.entity.AccountEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final AccountRepository accountRepository;

    @Transactional
    public TransactionResultType performTransaction(final Transaction transaction) {
        final UUID fromAccountId = transaction.fromAccountNumber();
        final UUID toAccountId = transaction.toAccountNumber();
        final AccountEntity fromAccount = accountRepository.getAccountEntityByNumber(fromAccountId);
        final AccountEntity toAccount = accountRepository.getAccountEntityByNumber(toAccountId);

        if (fromAccount == null || toAccount == null) {
            throw new AccountEntityNotFoundException(String.format("One of the accounts in this transaction is not found, %s, %s", fromAccountId, toAccountId));
        }

        if (hasInsufficientFunds(transaction, fromAccount)) {
            return TransactionResultType.INSUFFICIENT_FUNDS;
        }

        fromAccount.setAmount(fromAccount.getAmount().min(transaction.amount()));
        toAccount.setAmount(toAccount.getAmount().add(transaction.amount()));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
        return TransactionResultType.SUCCESSFUL;
    }

    private static boolean hasInsufficientFunds(final Transaction transaction, final AccountEntity fromAccount) {
        return fromAccount.getAmount()
                .compareTo(transaction.amount()) < 0;
    }
}
