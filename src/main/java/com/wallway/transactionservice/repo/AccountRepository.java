package com.wallway.transactionservice.repo;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wallway.transactionservice.repo.entity.AccountEntity;

public interface AccountRepository extends JpaRepository<AccountEntity, UUID> {
    AccountEntity getAccountEntityByNumber(final UUID accountNumber);

    Optional<AccountEntity> getAccountEntityByNameAndEmail(final String name, final String email);
}
