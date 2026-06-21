package com.thomasmckenna.tiny.ledger.controller.dtos;

import com.thomasmckenna.tiny.ledger.domain.TransactionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Getter
public class TransactionRequestDto {
    private final TransactionType transactionType;
    private final BigDecimal amount;
}
