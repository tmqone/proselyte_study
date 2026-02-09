package com.tmq.module_25.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@Builder(toBuilder = true)
public class UserEntity {
    @Id
    private Long id;
    private String username;
    private String password;
    private UserRole role;
    private UserStatus status;
    private EventEntity events;

    @ToString.Include(name = "password")
    private String maskPassword(){
        return "*password is hidden*";
    }
}
