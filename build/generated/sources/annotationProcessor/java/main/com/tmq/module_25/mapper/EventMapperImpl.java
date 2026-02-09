package com.tmq.module_25.mapper;

import com.tmq.module_25.dto.EventDto;
import com.tmq.module_25.dto.FileDto;
import com.tmq.module_25.entity.EventEntity;
import com.tmq.module_25.entity.FileEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-09T21:14:31+0300",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-9.3.0.jar, environment: Java 25.0.1 (Eclipse Adoptium)"
)
@Component
public class EventMapperImpl implements EventMapper {

    @Override
    public EventDto map(EventEntity eventEntity) {
        if ( eventEntity == null ) {
            return null;
        }

        EventDto eventDto = new EventDto();

        return eventDto;
    }

    @Override
    public FileEntity map(FileDto fileDto) {
        if ( fileDto == null ) {
            return null;
        }

        FileEntity.FileEntityBuilder fileEntity = FileEntity.builder();

        fileEntity.id( fileDto.getId() );
        fileEntity.name( fileDto.getName() );
        fileEntity.location( fileDto.getLocation() );
        fileEntity.status( fileDto.getStatus() );

        return fileEntity.build();
    }
}
