package com.tmq.module_25.mapper;

import com.tmq.module_25.dto.UserDto;
import com.tmq.module_25.entity.UserEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto map (UserEntity userEntity);
    @InheritInverseConfiguration
    UserEntity map (UserDto userDto);
}
