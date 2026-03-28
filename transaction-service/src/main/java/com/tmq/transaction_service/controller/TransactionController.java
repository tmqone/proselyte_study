package com.tmq.transaction_service.controller;

import com.tmq.person.service.api.TransactionsApi;
import com.tmq.person.service.dto.*;
import org.jspecify.annotations.Nullable;
import org.springframework.http.ResponseEntity;

import java.time.OffsetDateTime;
import java.util.UUID;

public class TransactionController implements TransactionsApi {
    @Override
    public ResponseEntity<TransactionConfirmResponse> confirmTransaction(PaymentType type, ConfirmTransactionRequest confirmTransactionRequest) {
        return null;
    }

    @Override
    public ResponseEntity<TransactionResponse> getTransactionStatus(UUID transactionUid) {
        return null;
    }

    @Override
    public ResponseEntity<TransactionPage> getTransactions(@Nullable UUID walletUid, @Nullable PaymentType type, @Nullable TransactionStatus status, @Nullable OffsetDateTime dateFrom, @Nullable OffsetDateTime dateTo, Integer page, Integer size) {
        return null;
    }

    @Override
    public ResponseEntity<TransactionInitResponse> initTransaction(PaymentType type, InitTransactionRequest initTransactionRequest) {
        return null;
    }
}
