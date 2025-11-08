package com.tmq.dto.entity;

import com.tmq.model.Action;

public record EventWithoutUserDto(Integer id, FileDto file, Action action){
}
