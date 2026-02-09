package com.tmq.module_25.mapper;

import com.tmq.module_25.dto.FileDto;
import com.tmq.module_25.entity.FileEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FileMapper {
    FileDto map (FileEntity fileEntity);
    @InheritInverseConfiguration
    FileEntity map (FileDto fileDto);
}
