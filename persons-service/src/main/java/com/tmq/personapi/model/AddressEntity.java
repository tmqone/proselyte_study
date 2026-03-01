package com.tmq.personapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "addresses")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private UUID id;
    private LocalDateTime created;
    private LocalDateTime updated;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private CountryEntity country;
    private String address;
    @Column(name = "zip_code")
    private String zipCode;
    private LocalDateTime archived;
    private String city;
    private String state;
}
