package com.tmq.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private char[] password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Event> events = new ArrayList<>();
}
