package com.tmq.dto.user;

import com.tmq.model.Role;

public record CreateUserRequest(String username, char[] password, Role role){
}
