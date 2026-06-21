package com.thomasmckenna.tiny.ledger.domain;

import com.thomasmckenna.tiny.ledger.exception.InsufficientFundsException;
import com.thomasmckenna.tiny.ledger.exception.InvalidAmountException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class Account {

    private static final int SCALE = 2;
    private final UUID id;
    private final List<Transaction> transactions = new ArrayList<>();

    public synchronized Transaction deposit(BigDecimal amount) {
        validateAmount(amount);
        Transaction depositTransaction = new Transaction(TransactionType.DEPOSIT, amount);
        transactions.add(depositTransaction);
        return depositTransaction;
    }

    public synchronized Transaction withdraw(BigDecimal amount) {
        validateAmount(amount);
        Transaction withdrawTransaction = new Transaction(TransactionType.WITHDRAW, amount);
        BigDecimal currentBalance = getBalance();
        if (currentBalance.compareTo(amount) < 0) {
            throw new InsufficientFundsException(withdrawTransaction.getId(), currentBalance, amount);
        }
        transactions.add(withdrawTransaction);
        return withdrawTransaction;
    }

    public synchronized BigDecimal getBalance() {
        BigDecimal total = BigDecimal.ZERO.setScale(SCALE);
        for (Transaction transaction: transactions) {
            total = total.add(transaction.getSignedAmount());
        }
        return total;
    }

    public synchronized List<Transaction> getTransactions() {
        return List.copyOf(transactions);
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null) {
            throw new InvalidAmountException("No transaction amount provided");
        }
        if (amount.signum() <= 0) {
            throw new InvalidAmountException("Transaction amount must be greater than zero");
        }
        if (amount.scale() > SCALE) {
            throw new InvalidAmountException("Transaction amount must have no more than " + SCALE + " decimal places");
        }
    }
}
