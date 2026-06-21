package com.thomasmckenna.tiny.ledger.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class Transaction {

    private final UUID id = UUID.randomUUID();
    private final TransactionType transactionType;
    private final BigDecimal amount;
    private final Instant timestamp = Instant.now();


    public BigDecimal getSignedAmount() {
        return transactionType.getSignedAmount(amount);
    }
}
