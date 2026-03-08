package com.tmq.person.service.mapper;

import com.tmq.person.service.dto.IndividualDto;
import com.tmq.person.service.dto.IndividualPageDto;
import com.tmq.person.service.dto.IndividualWriteDto;
import com.tmq.person.service.dto.IndividualWriteResponseDto;
import com.tmq.person.service.model.IndividualsEntity;
import org.mapstruct.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {AddressMapper.class})
public interface IndividualsMapper {

    @Mapping(source = "user.firstName", target = "firstName")
    @Mapping(source = "user.lastName", target = "lastName")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.address", target = "address")
    IndividualDto toDto(IndividualsEntity entity);

    @Mapping(source = "id", target = "id", qualifiedByName = "uuidToString")
    IndividualWriteResponseDto toWriteResponseDto(IndividualsEntity entity);

    @Mapping(source = "firstName", target = "user.firstName")
    @Mapping(source = "lastName", target = "user.lastName")
    @Mapping(source = "email", target = "user.email")
    @Mapping(source = "address", target = "user.address")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user.id", ignore = true)
    @Mapping(target = "user.secretKey", ignore = true)
    @Mapping(target = "user.created", ignore = true)
    @Mapping(target = "user.updated", ignore = true)
    @Mapping(target = "user.filled", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "verifiedAt", ignore = true)
    @Mapping(target = "archivedAt", ignore = true)
    IndividualsEntity toEntity(IndividualWriteDto dto);

    default IndividualPageDto toPageDto(List<IndividualsEntity> entities) {
        IndividualPageDto page = new IndividualPageDto();
        page.setItems(entities.stream().map(this::toDto).collect(Collectors.toList()));
        return page;
    }

    @AfterMapping
    default void setDefaults(@MappingTarget IndividualsEntity entity) {
        LocalDateTime now = LocalDateTime.now();
        if (entity.getStatus() == null) entity.setStatus("ACTIVE");
        if (entity.getVerifiedAt() == null) entity.setVerifiedAt(now);
        if (entity.getArchivedAt() == null) entity.setArchivedAt(now);
        if (entity.getUser() != null) {
            if (entity.getUser().getCreated() == null) entity.getUser().setCreated(now);
            if (entity.getUser().getUpdated() == null) entity.getUser().setUpdated(now);
        }
    }

    @Named("uuidToString")
    default String uuidToString(UUID uuid) {
        return uuid != null ? uuid.toString() : null;
    }
}