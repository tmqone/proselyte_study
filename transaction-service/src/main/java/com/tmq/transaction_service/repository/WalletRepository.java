package com.tmq.transaction_service.repository;

import com.tmq.transaction_service.entity.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WalletRepository extends JpaRepository<WalletEntity, UUID> {
    List<WalletEntity> findAllByUserId(UUID uuid);
}
