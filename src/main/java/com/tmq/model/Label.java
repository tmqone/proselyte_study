package com.tmq.model;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@EqualsAndHashCode @ToString
@Builder
public class Label {
    private Long id;
    private String name;
    private Status status;
}
