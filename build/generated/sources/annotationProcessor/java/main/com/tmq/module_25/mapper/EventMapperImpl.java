package com.tmq.module_25.mapper;

import com.tmq.module_25.dto.EventDto;
import com.tmq.module_25.entity.EventEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-10T12:37:34+0300",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-9.3.0.jar, environment: Java 25.0.2 (Homebrew)"
)
@Component
public class EventMapperImpl implements EventMapper {

    @Override
    public EventDto map(EventEntity eventEntity) {
        if ( eventEntity == null ) {
            return null;
        }

        EventDto.EventDtoBuilder eventDto = EventDto.builder();

        eventDto.id( eventEntity.getId() );
        eventDto.userId( eventEntity.getUserId() );
        eventDto.fileId( eventEntity.getFileId() );
        eventDto.status( eventEntity.getStatus() );
        eventDto.timestamp( eventEntity.getTimestamp() );

        return eventDto.build();
    }

    @Override
    public EventEntity map(EventDto eventDto) {
        if ( eventDto == null ) {
            return null;
        }

        EventEntity.EventEntityBuilder eventEntity = EventEntity.builder();

        eventEntity.id( eventDto.getId() );
        eventEntity.userId( eventDto.getUserId() );
        eventEntity.fileId( eventDto.getFileId() );
        eventEntity.status( eventDto.getStatus() );
        eventEntity.timestamp( eventDto.getTimestamp() );

        return eventEntity.build();
    }
}
