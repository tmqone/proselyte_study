package com.tmq.dto.user;


import com.tmq.model.Role;

public record FindUserWithAllFields(Integer id, String username, char[] password, Role role){
}
