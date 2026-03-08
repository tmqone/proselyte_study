package com.tmq.person.service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "individuals")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Audited
public class IndividualsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @OneToOne(cascade = CascadeType.ALL)
    private UserEntity user;
    @Column(name = "passport_number")
    private String passportNumber;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;
    @Column(name = "archived_at")
    private LocalDateTime archivedAt;
    private String status;
}
