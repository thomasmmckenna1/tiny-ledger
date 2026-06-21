package com.thomasmckenna.tiny.ledger.controller.dtos;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ErrorResponseDto {
    private final int status;
    private final String error;
    private final String message;
}
