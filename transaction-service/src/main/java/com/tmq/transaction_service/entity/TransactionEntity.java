package com.tmq.transaction_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "transactions")
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uid")
    private UUID id;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "modified_at", nullable = false)
    private Instant modifiedAt;

    @Column(name = "user_uid", nullable = false)
    private UUID userId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "wallet_uid", nullable = false)
    private WalletEntity wallet;

    @Column(name = "amount",nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private PaymentTypeEntity paymentType;

    @Column(name = "status", nullable = false, length = 32)
    private String status;

    @Column(name = "comment", length = 256)
    private String comment;

    @Column(name = "fee")
    private BigDecimal fee;

    @Column(name = "target_wallet_uid")
    private UUID targetWalledId;
    @Column(name = "payment_method_id")
    private Integer paymentMethodId;
    @Column(name = "failure_reason", length = 256)
    private String failureReason;

}
