package com.tmq.model;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@Getter @Setter
@ToString(exclude = "posts") @EqualsAndHashCode
@Builder
public class Writer {
    private Long id;
    private String firstName;
    private String lastName;
    private List<Post> posts;
}
