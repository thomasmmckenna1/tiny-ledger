package com.thomasmckenna.tiny.ledger.domain;

import java.math.BigDecimal;

public enum TransactionType {

    DEPOSIT {
        @Override
        public BigDecimal getSignedAmount(BigDecimal amount) { return amount; }
    },

    WITHDRAW {
        @Override
        public BigDecimal getSignedAmount(BigDecimal amount) { return amount.negate(); }
    };

    public abstract BigDecimal getSignedAmount(BigDecimal amount);
}
