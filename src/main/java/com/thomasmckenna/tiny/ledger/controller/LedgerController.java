package com.thomasmckenna.tiny.ledger.controller;

import com.thomasmckenna.tiny.ledger.controller.dtos.*;
import com.thomasmckenna.tiny.ledger.domain.Account;
import com.thomasmckenna.tiny.ledger.domain.Transaction;
import com.thomasmckenna.tiny.ledger.service.LedgerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class LedgerController {

    private final LedgerService ledgerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountResponseDto createAccount() {
        return AccountResponseDto.from(ledgerService.createAccount());
    }

    @GetMapping
    public List<AccountResponseDto> getAllAccounts() {
        return ledgerService.getAllAccounts().stream().map(AccountResponseDto::from).toList();
    }

    @GetMapping("/{accountId}/balance")
    public AccountResponseDto getAccountBalance(@PathVariable UUID accountId) {
        return new AccountResponseDto(accountId, ledgerService.getAccountBalance(accountId));
    }

    @PostMapping("/{accountId}/transactions")
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponseDto transaction(@PathVariable UUID accountId, @RequestBody TransactionRequestDto transactionRequest) {
        Transaction transaction = switch (transactionRequest.getTransactionType()) {
            case DEPOSIT -> ledgerService.deposit(accountId, transactionRequest.getAmount());
            case WITHDRAW -> ledgerService.withdraw(accountId, transactionRequest.getAmount());
        };

        BigDecimal newBalance = ledgerService.getAccountBalance(accountId);
        return TransactionResponseDto.from(transaction, newBalance);
    }

    @GetMapping("/{accountId}/transactions")
    public TransactionHistoryResponseDto getAccountTransactions(@PathVariable UUID accountId) {
        Account account = ledgerService.getAccount(accountId);
        List<Transaction> transactions = ledgerService.getAccountTransactions(accountId);
        List<TransactionResponseDto> transactionHistory = new ArrayList<>();
        BigDecimal runningBalance = BigDecimal.ZERO.setScale(2);
        for (Transaction transaction: transactions) {
            runningBalance = runningBalance.add(transaction.getSignedAmount());
            transactionHistory.add(TransactionResponseDto.from(transaction, runningBalance));
        }
        return new TransactionHistoryResponseDto(AccountResponseDto.from(account), transactionHistory);
    }
}
