package com.tmq.module_25.mapper;

import com.tmq.module_25.dto.FileDto;
import com.tmq.module_25.entity.FileEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-10T10:03:28+0300",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-9.3.0.jar, environment: Java 25.0.2 (Homebrew)"
)
@Component
public class FileMapperImpl implements FileMapper {

    @Override
    public FileDto map(FileEntity fileEntity) {
        if ( fileEntity == null ) {
            return null;
        }

        FileDto fileDto = new FileDto();

        fileDto.setId( fileEntity.getId() );
        fileDto.setName( fileEntity.getName() );
        fileDto.setLocation( fileEntity.getLocation() );
        fileDto.setStatus( fileEntity.getStatus() );

        return fileDto;
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
