package com.tmq.mapper;

import com.tmq.dto.WriterDto;
import com.tmq.model.Writer;

public class WriterMapper implements Mapper<Writer, WriterDto> {

    @Override
    public Writer toEntity(WriterDto writerDto) {
        return Writer.builder()
                .id(writerDto.id())
                .lastName(writerDto.lastName())
                .firstName(writerDto.firstName())
                .build();
    }

    @Override
    public WriterDto fromEntity(Writer writer) {
        return WriterDto.builder()
                .id(writer.getId())
                .lastName(writer.getLastName())
                .firstName(writer.getFirstName())
                .build();
    }
}
