package com.thomasmckenna.tiny.ledger.service;

import com.thomasmckenna.tiny.ledger.domain.Account;
import com.thomasmckenna.tiny.ledger.domain.Transaction;
import com.thomasmckenna.tiny.ledger.exception.AccountNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LedgerService {

    private final Map<UUID, Account> accounts = new ConcurrentHashMap<>();

    public Account createAccount() {
        Account account = new Account(UUID.randomUUID());
        accounts.put(account.getId(), account);
        return account;
    }

    public Account getAccount(UUID id) {

        Account account = accounts.get(id);
        if (account == null) {
            throw new AccountNotFoundException(id);
        }
        return accounts.get(id);
    }

    public Transaction deposit(UUID accountId, BigDecimal amount) {
        return getAccount(accountId).deposit(amount);
    }

    public Transaction withdraw(UUID accountId, BigDecimal amount) {
        return getAccount(accountId).withdraw(amount);
    }

    public BigDecimal getAccountBalance(UUID accountID) {
        return getAccount(accountID).getBalance();
    }

    public List<Transaction> getAccountTransactions(UUID accountId) {
        return getAccount(accountId).getTransactions();
    }

    public List<Account> getAllAccounts() {
        return accounts.values().stream().toList();
    }
}
