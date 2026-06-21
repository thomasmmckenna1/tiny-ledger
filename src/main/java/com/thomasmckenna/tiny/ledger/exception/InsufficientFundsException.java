package com.thomasmckenna.tiny.ledger.exception;

import java.math.BigDecimal;
import java.util.UUID;

public class InsufficientFundsException extends RuntimeException{

    public InsufficientFundsException(UUID accountId, BigDecimal balance, BigDecimal requested) {
        super("Account " + accountId + " balance too low. Balance: "
                + balance.toPlainString() + ". Withdrawal requested: "
                + requested.toPlainString());
    }
}
