package com.wallway.transactionservice.repo.entity;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class AccountEntity {
            @Id
            @GeneratedValue(strategy = GenerationType.AUTO)
            private UUID number;

            @Column(nullable = false)
            private String name;

            @Column(nullable = false)
            private String email;

            @Column(nullable = false)
            private BigDecimal amount;
}
