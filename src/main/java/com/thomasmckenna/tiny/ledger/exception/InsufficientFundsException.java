package com.thomasmckenna.tiny.ledger.exception;

import java.math.BigDecimal;
import java.util.UUID;

public class InsufficientFundsException extends RuntimeException{

    public InsufficientFundsException(UUID transactionId, BigDecimal balance, BigDecimal requested) {
        super("Transaction " + transactionId + " failed. Account balance too low. Balance: "
                + balance.toPlainString() + ". Withdrawal requested: "
                + requested.toPlainString());
    }
}
