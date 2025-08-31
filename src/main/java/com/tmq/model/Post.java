package com.tmq.model;

import lombok.*;

import java.util.List;

@AllArgsConstructor @NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode @ToString
@Builder
public class Post {
    private Long id;
    private String title;
    private String content;
    private List<Label> labels;
    private Status status;
}
