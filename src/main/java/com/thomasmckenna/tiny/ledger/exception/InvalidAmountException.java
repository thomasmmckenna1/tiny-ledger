package com.thomasmckenna.tiny.ledger.exception;

public class InvalidAmountException extends RuntimeException{

    public InvalidAmountException(String message) {
        super(message);
    }
}
