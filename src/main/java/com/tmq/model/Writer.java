package com.tmq.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@ToString(exclude = "posts") @EqualsAndHashCode
@Builder
@Entity
@Table(name = "writers")
public class Writer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @OneToMany(mappedBy = "writer", fetch = FetchType.LAZY)
    private List<Post> posts;
}
