package com.tmq.transaction_service.mapper;

import com.tmq.person.service.dto.*;
import com.tmq.transaction_service.entity.PaymentTypeEntity;
import com.tmq.transaction_service.entity.TransactionEntity;
import com.tmq.transaction_service.entity.TransactionStatusEntity;
import com.tmq.transaction_service.entity.WalletEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "userId", source = "initRequest.userId")
    @Mapping(target = "wallet", source = "wallet")
    @Mapping(target = "amount", source = "initRequest.amount")
    @Mapping(target = "paymentType", source = "paymentType")
    @Mapping(target = "comment", source = "initRequest.comment")
    @Mapping(target = "fee", source = "fee")
    @Mapping(target = "paymentMethodId", source = "paymentMethodId")
    TransactionEntity toEntity(DepositOrWithdrawalInitRequest initRequest,
                               WalletEntity wallet,
                               PaymentTypeEntity paymentType,
                               BigDecimal fee,
                               Integer paymentMethodId);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "userId", source = "transferInitRequest.userId")
    @Mapping(target = "wallet", source = "wallet")
    @Mapping(target = "amount", source = "transferInitRequest.amount")
    @Mapping(target = "paymentType", source = "paymentType")
    @Mapping(target = "comment", source = "transferInitRequest.comment")
    @Mapping(target = "fee", source = "fee")
    @Mapping(target = "targetWalledId", source = "transferInitRequest.targetWalletUid")
    @Mapping(target = "paymentMethodId", source = "paymentMethodId")
    TransactionEntity toEntity(TransferInitRequest transferInitRequest,
                               WalletEntity wallet,
                               PaymentTypeEntity paymentType,
                               BigDecimal fee,
                               Integer paymentMethodId);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "transactionUid", source = "id")
    @Mapping(target = "walletUid", source = "wallet.id")
    @Mapping(target = "type", source = "paymentType")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "fee", source = "fee")
    @Mapping(target = "comment", source = "comment")
    TransactionInitResponse toInitResponse(TransactionEntity entity);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "uid", source = "id")
    @Mapping(target = "userUid", source = "userId")
    @Mapping(target = "walletUid", source = "wallet.id")
    @Mapping(target = "targetWalletUid", source = "targetWalledId")
    @Mapping(target = "paymentMethodId", source = "paymentMethodId")
    @Mapping(target = "type", source = "paymentType")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "fee", source = "fee")
    @Mapping(target = "comment", source = "comment")
    @Mapping(target = "failureReason", source = "failureReason")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "modifiedAt", source = "modifiedAt")
    TransactionResponse toResponse(TransactionEntity entity);

    default OffsetDateTime map(Instant instant) {
        if (instant == null) return null;
        return instant.atOffset(ZoneOffset.UTC);
    }

    @AfterMapping
    default void setDefaults(@MappingTarget TransactionEntity entity) {
        if (entity.getCreatedAt() == null) entity.setCreatedAt(Instant.now());
        if (entity.getStatus() == null) entity.setStatus(TransactionStatusEntity.NEW);
        entity.setModifiedAt(Instant.now());
    }
}
