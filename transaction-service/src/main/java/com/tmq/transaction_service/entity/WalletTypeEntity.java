package com.tmq.transaction_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Table(name = "wallet_types")
@Entity
public class WalletTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uid")
    private UUID id;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "modified_at", nullable = false)
    private Instant modifiedAt;

    @Column(name = "name", nullable = false, length = 32)
    private String name;

    @Column(name = "currency_code", nullable = false, length = 3)
    private String currencyCode;

    @Column(name = "status", nullable = false, length = 18)
    private String status;

    @Column(name = "archived_at")
    private Instant archivedAt;

    @Column(name = "user_type", length = 15)
    private String userType;

    @Column(name = "creator", length = 255)
    private String creator;

    @Column(name = "modifier", length = 255)
    private String modifier;
}
