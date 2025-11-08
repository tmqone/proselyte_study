package com.tmq.dto.entity;

import com.tmq.model.Action;

public record EventWithoutFileDesc (Integer id, Integer fileId, Action action){
}
