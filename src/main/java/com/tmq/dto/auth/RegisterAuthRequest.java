package com.tmq.dto.auth;

import com.tmq.model.Role;

public record RegisterAuthRequest (String username, char[] password) {
}
