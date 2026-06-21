package com.thomasmckenna.tiny.ledger.controller.dtos;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class TransactionHistoryResponseDto {

    private final AccountResponseDto account;
    private final List<TransactionResponseDto> transactions;
}
