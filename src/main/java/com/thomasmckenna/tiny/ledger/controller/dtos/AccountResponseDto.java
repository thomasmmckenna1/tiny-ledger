package com.thomasmckenna.tiny.ledger.controller.dtos;

import com.thomasmckenna.tiny.ledger.domain.Account;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class AccountResponseDto {
    private final UUID accountId;
    private final BigDecimal balance;

    public static AccountResponseDto from(Account account) {
        return new AccountResponseDto(account.getId(), account.getBalance());
    }
}
