package com.tmq.transaction_service.service;

import com.tmq.transaction_service.entity.WalletEntity;
import com.tmq.transaction_service.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletService {
    private final WalletRepository walletRepository;

    public WalletEntity createWallet(WalletEntity entity) {
        return walletRepository.save(entity);
    }

    public WalletEntity findById(UUID walletUid) {
        //TODO переделать исключение
        return walletRepository.findById(walletUid).orElseThrow(() -> new RuntimeException("Wallet not found"));
    }

    public List<WalletEntity> findByUserId(UUID userUid) {
        return walletRepository.findAllByUserId(userUid);
    }
}
