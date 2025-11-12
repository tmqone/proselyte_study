package com.tmq.dto.user;

import com.tmq.model.Role;


public record FindAllUserResponse(Integer id, String username, Role role) {
}
