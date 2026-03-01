package com.tmq.personapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "individuals")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndividualsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private UUID id;
    @OneToOne(cascade = CascadeType.ALL)
    private UserEntity user;
    @Column(name = "passport_number")
    private String passportNumber;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "verified_at")
    private String verifiedAt;
    @Column(name = "archived_at")
    private String archivedAt;
    private String status;
}
