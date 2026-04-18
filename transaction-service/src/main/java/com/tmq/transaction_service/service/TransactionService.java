package com.tmq.transaction_service.service;

import com.tmq.person.service.dto.*;
import com.tmq.transaction_service.entity.PaymentTypeEntity;
import com.tmq.transaction_service.entity.TransactionEntity;
import com.tmq.transaction_service.entity.WalletEntity;
import com.tmq.transaction_service.mapper.TransactionMapper;
import com.tmq.transaction_service.repository.TransactionRepository;
import com.tmq.transaction_service.validator.TransactionValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final WalletService walletService;
    private final TransactionMapper transactionMapper;
    private final TransactionValidator transactionValidator;

    @Transactional
    public TransactionEntity initTransaction(PaymentType paymentType, InitTransactionRequest initTransactionRequest){
        return switch (paymentType){
            case DEPOSIT -> initDepositTransaction(paymentType, initTransactionRequest);
            case WITHDRAWAL -> initWithdrawalTransaction(paymentType, initTransactionRequest);
            case TRANSFER -> initTransferTransaction(paymentType, initTransactionRequest);
        };
    }

    public TransactionEntity confirmTransaction(PaymentType paymentType, UUID transactionId){
        return switch (paymentType){
            case DEPOSIT -> null;
            case TRANSFER -> null;
            case WITHDRAWAL -> null;
        };
    }



    public TransactionEntity getTransaction(UUID uuid) {
        //TODO переделать exception
        return transactionRepository.findById(uuid).orElseThrow(RuntimeException::new);
    }

    private TransactionEntity initDepositTransaction(PaymentType paymentType, InitTransactionRequest initTransactionRequest){
        DepositOrWithdrawalInitRequest DepositOrWithdrawalInitRequest = (DepositOrWithdrawalInitRequest) initTransactionRequest;
        WalletEntity wallet = walletService.findById(DepositOrWithdrawalInitRequest.getWalletUid());
        TransactionEntity transaction = transactionMapper.toEntity(
                DepositOrWithdrawalInitRequest,
                wallet,
                PaymentTypeEntity.DEPOSIT,
                countFee(),
                0
        );
        return transactionRepository.save(transaction);
    }

    private TransactionEntity initWithdrawalTransaction(PaymentType paymentType, InitTransactionRequest initTransactionRequest){
        DepositOrWithdrawalInitRequest depositOrWithdrawalInitRequest = (DepositOrWithdrawalInitRequest) initTransactionRequest;
        WalletEntity wallet = walletService.findById(depositOrWithdrawalInitRequest.getWalletUid());
        transactionValidator.isWalletBalanceAmountGreaterThanRequest(wallet, depositOrWithdrawalInitRequest.getAmount());

        TransactionEntity transaction = transactionMapper.toEntity(
                depositOrWithdrawalInitRequest,
                wallet,
                PaymentTypeEntity.WITHDRAWAL,
                countFee(),
                0
        );
        return transactionRepository.save(transaction);
    }

    private TransactionEntity initTransferTransaction(PaymentType paymentType, InitTransactionRequest initTransactionRequest){
        TransferInitRequest transferInitRequest = (TransferInitRequest) initTransactionRequest;
        WalletEntity sourceWallet = walletService.findById(transferInitRequest.getSourceWalletUid());
        transactionValidator.isWalletBalanceAmountGreaterThanRequest(sourceWallet, transferInitRequest.getAmount());
        WalletEntity targetWallet = walletService.findById(transferInitRequest.getTargetWalletUid());
        TransactionEntity entity = transactionMapper.toEntity(
                transferInitRequest,
                sourceWallet,
                PaymentTypeEntity.TRANSFER,
                countFee(),
                0
        );
        return transactionRepository.save(entity);
    }

    private BigDecimal countFee(){
        return BigDecimal.ZERO;
    }

}
