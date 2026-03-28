package com.tmq.transaction_service.mapper;

import com.tmq.person.service.dto.CreateWalletRequest;
import com.tmq.person.service.dto.WalletResponse;
import com.tmq.transaction_service.entity.WalletEntity;
import com.tmq.transaction_service.entity.WalletTypeEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface WalletMapper {

    @Mapping(source = "walletTypeUid", target = "walletTypeEntity")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "archivedAt", ignore = true)
    WalletEntity toEntity(CreateWalletRequest dto);

    @Mapping(source = "userId", target = "userUid")
    @Mapping(source = "walletTypeEntity.id", target = "walletTypeUid")
    @Mapping(source = "id", target = "uid")
    WalletResponse toDto(WalletEntity entity);

    default WalletTypeEntity map(UUID id) {
        if (id == null) return null;
        return WalletTypeEntity.builder().id(id).build();
    }

    default OffsetDateTime map(Instant instant) {
        if (instant == null) return null;
        return instant.atOffset(ZoneOffset.UTC);
    }

    @AfterMapping
    default void setDefaults(@MappingTarget WalletEntity entity) {
        if (entity.getStatus() == null) entity.setStatus("ACTIVE");
        if (entity.getCreatedAt() == null) entity.setCreatedAt(Instant.now());
        if (entity.getBalance() == null) entity.setBalance(BigDecimal.ZERO);
    }
}