package com.thomasmckenna.tiny.ledger.service;

import com.thomasmckenna.tiny.ledger.domain.Account;
import com.thomasmckenna.tiny.ledger.exception.AccountNotFoundException;
import com.thomasmckenna.tiny.ledger.exception.InvalidAmountException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LedgerServiceTest {

    private LedgerService ledgerService;

    @BeforeEach
    void setUp() {
        ledgerService = new LedgerService();
    }

    @Test
    void createAccountReturnsNewAccountWithZeroBalance() {
        Account account = ledgerService.createAccount();

        assertThat(account).isNotNull();
        assertThat(account.getId()).isNotNull();
        assertThat(account.getBalance()).isEqualByComparingTo("0.00");
    }

    @Test
    void getAccountReturnsPreviouslyCreatedAccount() {
        Account created = ledgerService.createAccount();

        Account fetched = ledgerService.getAccount(created.getId());

        assertThat(fetched).isSameAs(created);
    }

    @Test
    void getAccountThrowsAccountNotFoundExceptionForUnknownId() {
        UUID unknownId = UUID.randomUUID();

        assertThatThrownBy(() -> ledgerService.getAccount(unknownId))
                .isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    void depositAddsTransactionAndIncreasesBalance() {
        Account account = ledgerService.createAccount();

        ledgerService.deposit(account.getId(), new BigDecimal("50.00"));

        assertThat(ledgerService.getAccountBalance(account.getId())).isEqualByComparingTo("50.00");
    }

    @Test
    void withdrawDecreasesBalance() {
        Account account = ledgerService.createAccount();
        ledgerService.deposit(account.getId(), new BigDecimal("50.00"));

        ledgerService.withdraw(account.getId(), new BigDecimal("20.00"));

        assertThat(ledgerService.getAccountBalance(account.getId())).isEqualByComparingTo("30.00");
    }

    @Test
    void depositForUnknownAccountThrowsAccountNotFoundException() {
        UUID unknownId = UUID.randomUUID();

        assertThatThrownBy(() -> ledgerService.deposit(unknownId, new BigDecimal("10.00")))
                .isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    void depositInvalidAmountThrowsInvalidAmountException() {
        Account account = ledgerService.createAccount();

        assertThatThrownBy(() -> ledgerService.deposit(account.getId(), new BigDecimal("-5.00")))
                .isInstanceOf(InvalidAmountException.class);
    }

    @Test
    void getAccountTransactionsReturnsRecordedTransactions() {
        Account account = ledgerService.createAccount();
        ledgerService.deposit(account.getId(), new BigDecimal("100.00"));
        ledgerService.withdraw(account.getId(), new BigDecimal("25.00"));

        assertThat(ledgerService.getAccountTransactions(account.getId())).hasSize(2);
    }

    @Test
    void getAllAccountsReturnsAllCreatedAccounts() {
        Account first = ledgerService.createAccount();
        Account second = ledgerService.createAccount();

        assertThat(ledgerService.getAllAccounts())
                .hasSize(2)
                .extracting(Account::getId)
                .containsExactlyInAnyOrder(first.getId(), second.getId());
    }
}