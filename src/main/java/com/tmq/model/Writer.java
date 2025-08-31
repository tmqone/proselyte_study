package com.tmq.model;

import lombok.*;

import java.util.List;

@AllArgsConstructor @NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode @ToString
@Builder
public class Writer {
    private Long id;
    private String firstName;
    private String lastName;
    private List<Post> posts;
    private Status status;
}
