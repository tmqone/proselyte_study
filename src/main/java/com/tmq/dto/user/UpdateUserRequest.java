package com.tmq.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tmq.dto.entity.EventDto;
import com.tmq.model.Event;
import lombok.Builder;

import java.util.List;

public record UpdateUserRequest(Integer id, String username){
}
