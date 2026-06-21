package com.thomasmckenna.tiny.ledger.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thomasmckenna.tiny.ledger.controller.dtos.TransactionRequestDto;
import com.thomasmckenna.tiny.ledger.domain.Account;
import com.thomasmckenna.tiny.ledger.domain.Transaction;
import com.thomasmckenna.tiny.ledger.domain.TransactionType;
import com.thomasmckenna.tiny.ledger.exception.AccountNotFoundException;
import com.thomasmckenna.tiny.ledger.exception.InsufficientFundsException;
import com.thomasmckenna.tiny.ledger.service.LedgerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LedgerController.class)
class LedgerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private LedgerService ledgerService;

    @Test
    void createAccountReturns201WithAccountDetails() throws Exception {
        Account account = new Account(UUID.randomUUID());
        when(ledgerService.createAccount()).thenReturn(account);

        mockMvc.perform(post("/accounts"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountId").value(account.getId().toString()))
                .andExpect(jsonPath("$.balance").value(0.00));
    }

    @Test
    void getAllAccountsReturnsListOfAccounts() throws Exception {
        Account account = new Account(UUID.randomUUID());
        when(ledgerService.getAllAccounts()).thenReturn(List.of(account));

        mockMvc.perform(get("/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].accountId").value(account.getId().toString()));
    }

    @Test
    void getAccountBalanceReturnsBalance() throws Exception {
        UUID accountId = UUID.randomUUID();
        when(ledgerService.getAccountBalance(accountId)).thenReturn(new BigDecimal("75.50"));

        mockMvc.perform(get("/accounts/{accountId}/balance", accountId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value(accountId.toString()))
                .andExpect(jsonPath("$.balance").value(75.50));
    }

    @Test
    void getAccountBalanceForUnknownAccountReturns404() throws Exception {
        UUID accountId = UUID.randomUUID();
        when(ledgerService.getAccountBalance(accountId)).thenThrow(new AccountNotFoundException(accountId));

        mockMvc.perform(get("/accounts/{accountId}/balance", accountId))
                .andExpect(status().isNotFound());
    }

    @Test
    void depositTransactionReturns201WithNewBalance() throws Exception {
        UUID accountId = UUID.randomUUID();
        TransactionRequestDto requestDto = new TransactionRequestDto(TransactionType.DEPOSIT, new BigDecimal("100.00"));
        Transaction transaction = new Transaction(TransactionType.DEPOSIT, new BigDecimal("100.00"));

        when(ledgerService.deposit(eq(accountId), any(BigDecimal.class))).thenReturn(transaction);
        when(ledgerService.getAccountBalance(accountId)).thenReturn(new BigDecimal("100.00"));

        mockMvc.perform(post("/accounts/{accountId}/transactions", accountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.newBalance").value(100.00));
    }

    @Test
    void withdrawTransactionWithInsufficientFundsReturns422() throws Exception {
        UUID accountId = UUID.randomUUID();
        TransactionRequestDto requestDto = new TransactionRequestDto(TransactionType.WITHDRAW, new BigDecimal("100.00"));

        when(ledgerService.withdraw(eq(accountId), any(BigDecimal.class)))
                .thenThrow(new InsufficientFundsException(UUID.randomUUID(), BigDecimal.ZERO, new BigDecimal("100.00")));

        mockMvc.perform(post("/accounts/{accountId}/transactions", accountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void getAccountTransactionsReturnsTransactionHistory() throws Exception {
        UUID accountId = UUID.randomUUID();
        Account account = new Account(accountId);
        Transaction deposit = new Transaction(TransactionType.DEPOSIT, new BigDecimal("50.00"));

        when(ledgerService.getAccount(accountId)).thenReturn(account);
        when(ledgerService.getAccountTransactions(accountId)).thenReturn(List.of(deposit));

        mockMvc.perform(get("/accounts/{accountId}/transactions", accountId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.account.accountId").value(accountId.toString()))
                .andExpect(jsonPath("$.transactions[0].uuid").value(deposit.getId().toString()));
    }
}