package com.thomasmckenna.tiny.ledger.domain;

import com.thomasmckenna.tiny.ledger.exception.InsufficientFundsException;
import com.thomasmckenna.tiny.ledger.exception.InvalidAmountException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AccountTest {

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account(UUID.randomUUID());
    }

    @Test
    void depositIncreasesBalance() {
        account.deposit(new BigDecimal("100.00"));

        assertThat(account.getBalance()).isEqualByComparingTo("100.00");
    }

    @Test
    void withdrawDecreasesBalance() {
        account.deposit(new BigDecimal("100.00"));
        account.withdraw(new BigDecimal("40.00"));

        assertThat(account.getBalance()).isEqualByComparingTo("60.00");
    }

    @Test
    void withdrawMoreThanBalanceThrowsInsufficientFundsException() {
        account.deposit(new BigDecimal("50.00"));

        assertThatThrownBy(() -> account.withdraw(new BigDecimal("100.00")))
                .isInstanceOf(InsufficientFundsException.class);
    }

    @Test
    void depositNullAmountThrowsInvalidAmountException() {
        assertThatThrownBy(() -> account.deposit(null))
                .isInstanceOf(InvalidAmountException.class);
    }

    @Test
    void depositNegativeAmountThrowsInvalidAmountException() {
        assertThatThrownBy(() -> account.deposit(new BigDecimal("-10.00")))
                .isInstanceOf(InvalidAmountException.class);
    }

    @Test
    void depositAmountWithTooManyDecimalPlacesThrowsInvalidAmountException() {
        assertThatThrownBy(() -> account.deposit(new BigDecimal("10.005")))
                .isInstanceOf(InvalidAmountException.class);
    }

    @Test
    void getTransactionsReturnsRecordedTransactionsInOrder() {
        account.deposit(new BigDecimal("100.00"));
        account.withdraw(new BigDecimal("30.00"));

        assertThat(account.getTransactions()).hasSize(2);
        assertThat(account.getTransactions().get(0).getTransactionType()).isEqualTo(TransactionType.DEPOSIT);
        assertThat(account.getTransactions().get(1).getTransactionType()).isEqualTo(TransactionType.WITHDRAW);
    }

    @Test
    void getTransactionsReturnsImmutableCopy() {
        account.deposit(new BigDecimal("100.00"));

        assertThatThrownBy(() -> account.getTransactions().add(null))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void newAccountHasZeroBalance() {
        assertThat(account.getBalance()).isEqualByComparingTo("0.00");
    }
}