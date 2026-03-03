package com.tmq.personapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "addresses")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Audited
public class AddressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private LocalDateTime created;
    private LocalDateTime updated;
    @ManyToOne(fetch = FetchType.LAZY)
    private CountryEntity country;
    private String address;
    @Column(name = "zip_code")
    private String zipCode;
    private LocalDateTime archived;
    private String city;
    private String state;
}
