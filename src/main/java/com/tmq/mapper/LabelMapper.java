package com.tmq.mapper;

import com.tmq.dto.LabelDto;
import com.tmq.model.Label;

public class LabelMapper implements Mapper<Label, LabelDto>{
    @Override
    public Label toEntity(LabelDto labelDto) {
        return Label.builder()
                .id(labelDto.id())
                .name(labelDto.name())
                .build();
    }

    @Override
    public LabelDto fromEntity(Label label) {
        return LabelDto.builder()
                .id(label.getId())
                .name(label.getName())
                .build();
    }
}
