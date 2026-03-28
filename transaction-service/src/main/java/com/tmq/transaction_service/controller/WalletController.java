package com.tmq.transaction_service.controller;

import com.tmq.person.service.api.WalletsApi;
import com.tmq.person.service.dto.CreateWalletRequest;
import com.tmq.person.service.dto.WalletResponse;
import com.tmq.transaction_service.entity.WalletEntity;
import com.tmq.transaction_service.mapper.WalletMapper;
import com.tmq.transaction_service.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class WalletController implements WalletsApi {
    private final WalletMapper walletMapper;
    private final WalletService walletService;

    @Override
    public ResponseEntity<WalletResponse> createWallet(CreateWalletRequest createWalletRequest) {
        WalletEntity wallet = walletService.createWallet(walletMapper.toEntity(createWalletRequest));
        return ResponseEntity.status(201).body(walletMapper.toDto(wallet));
    }

    @Override
    public ResponseEntity<WalletResponse> getWalletByUid(UUID walletUid) {
        return ResponseEntity.ok(walletMapper.toDto(walletService.findById(walletUid)));
    }

    @Override
    public ResponseEntity<List<WalletResponse>> getWalletsByUserUid(UUID userUid) {
        List<WalletResponse> list = walletService.findByUserId(userUid).stream()
                .map(walletMapper::toDto)
                .toList();
        return ResponseEntity.ok(list);
    }
}
