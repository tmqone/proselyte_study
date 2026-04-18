package com.tmq.transaction_service.controller;

import com.tmq.person.service.api.TransactionsApi;
import com.tmq.person.service.dto.*;
import com.tmq.transaction_service.entity.TransactionEntity;
import com.tmq.transaction_service.mapper.TransactionMapper;
import com.tmq.transaction_service.mapper.WalletMapper;
import com.tmq.transaction_service.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TransactionController implements TransactionsApi {
    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;


    @Override
    public ResponseEntity<TransactionConfirmResponse> confirmTransaction(PaymentType type,
                                                                         TransactionConfirmRequest transactionConfirmRequest) {
        return null;
    }

    @Override
    public ResponseEntity<TransactionResponse> getTransactionStatus(UUID transactionUid) {
        TransactionEntity transaction = transactionService.getTransaction(transactionUid);
        TransactionResponse response = transactionMapper.toResponse(transaction);
        return ResponseEntity.ok().body(response);
    }

    @Override
    public ResponseEntity<TransactionPage> getTransactions(@Nullable UUID walletUid,
                                                           @Nullable PaymentType type,
                                                           @Nullable TransactionStatus status,
                                                           @Nullable OffsetDateTime dateFrom,
                                                           @Nullable OffsetDateTime dateTo,
                                                           Integer page,
                                                           Integer size) {
        return null;
    }

    @Override
    public ResponseEntity<TransactionInitResponse> initTransaction(PaymentType type, InitTransactionRequest initTransactionRequest) {
        TransactionEntity transaction = transactionService.initTransaction(type, initTransactionRequest);
        return ResponseEntity.ok().body(transactionMapper.toInitResponse(transaction));
    }
}
