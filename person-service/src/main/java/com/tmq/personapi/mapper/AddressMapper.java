package com.tmq.personapi.mapper;

import com.tmq.common.dto.AddressDto;
import com.tmq.common.dto.AddressWriteDto;
import com.tmq.personapi.model.AddressEntity;
import org.mapstruct.*;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", uses = {CountryMapper.class})
public interface AddressMapper {

    @Mapping(source = "country", target = "countryCode")
    AddressDto toDto(AddressEntity entity);

    @Mapping(source = "countryCode", target = "country")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true)
    @Mapping(target = "archived", ignore = true)
    AddressEntity toEntity(AddressWriteDto dto);

    @AfterMapping
    default void setDefaults(@MappingTarget AddressEntity entity) {
        LocalDateTime now = LocalDateTime.now();
        if (entity.getCreated() == null) entity.setCreated(now);
        if (entity.getUpdated() == null) entity.setUpdated(now);
        if (entity.getArchived() == null) entity.setArchived(now);
    }
}