package com.tmq.module_25.mapper;

import com.tmq.module_25.dto.UserDto;
import com.tmq.module_25.entity.UserEntity;
import com.tmq.module_25.entity.UserStatus;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-10T10:03:28+0300",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-9.3.0.jar, environment: Java 25.0.2 (Homebrew)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDto map(UserEntity userEntity) {
        if ( userEntity == null ) {
            return null;
        }

        UserDto userDto = new UserDto();

        userDto.setId( userEntity.getId() );
        userDto.setUsername( userEntity.getUsername() );
        userDto.setPassword( userEntity.getPassword() );
        userDto.setRole( userEntity.getRole() );
        if ( userEntity.getStatus() != null ) {
            userDto.setStatus( userEntity.getStatus().name() );
        }

        return userDto;
    }

    @Override
    public UserEntity map(UserDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        UserEntity.UserEntityBuilder userEntity = UserEntity.builder();

        userEntity.id( userDto.getId() );
        userEntity.username( userDto.getUsername() );
        userEntity.password( userDto.getPassword() );
        userEntity.role( userDto.getRole() );
        if ( userDto.getStatus() != null ) {
            userEntity.status( Enum.valueOf( UserStatus.class, userDto.getStatus() ) );
        }

        return userEntity.build();
    }
}
