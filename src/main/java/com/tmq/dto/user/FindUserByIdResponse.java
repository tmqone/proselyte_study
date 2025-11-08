package com.tmq.dto.user;

import com.tmq.dto.entity.EventWithoutFileDesc;
import com.tmq.dto.entity.EventWithoutUserDto;

import java.util.List;

public record FindUserByIdResponse(Integer id, String username){
}
