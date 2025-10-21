package com.tmq.model;
import lombok.*;

import java.time.Instant;
import java.util.List;


@AllArgsConstructor
@Getter @Setter
@ToString @EqualsAndHashCode
@Builder
public class Post {
    private Long id;
    private String content;
    private Instant created;
    private Instant updated;
    private Writer writer;
    private List<Label> labels;
    private PostStatus postStatus;
}
