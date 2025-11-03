package com.tmq.model;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@ToString @EqualsAndHashCode
@Builder
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    @Column(name = "created_at")
    private Instant created;
    @Column(name = "updated_at")
    private Instant updated;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private Writer writer;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "posts_labels",
            joinColumns = {@JoinColumn(name = "post_id")},
            inverseJoinColumns = {@JoinColumn(name = "label_id")})
    private List<Label> labels;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PostStatus postStatus;
}
