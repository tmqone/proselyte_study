package com.tmq.model;

import lombok.*;

@AllArgsConstructor
@Getter @Setter
@ToString @EqualsAndHashCode
@Builder
public class Label {
    private Long id;
    private String name;
}
