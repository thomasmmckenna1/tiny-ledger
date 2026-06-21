package com.thomasmckenna.tiny.ledger.controller.dtos;

import com.thomasmckenna.tiny.ledger.domain.Transaction;
import com.thomasmckenna.tiny.ledger.domain.TransactionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class TransactionResponseDto {

    private final UUID uuid;
    private final TransactionType transactionType;
    private final BigDecimal amount;
    private final BigDecimal newBalance;
    private final Instant timestamp;

    public static TransactionResponseDto from(Transaction transaction, BigDecimal newBalance) {
        return new TransactionResponseDto(
                transaction.getId(),
                transaction.getTransactionType(),
                transaction.getAmount(),
                newBalance,
                transaction.getTimestamp());
    }
}
