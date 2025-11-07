package com.tmq.mapper;

import com.tmq.dto.entity.EventDto;
import com.tmq.dto.entity.UserDto;
import com.tmq.dto.user.*;
import com.tmq.model.User;

public class UserMapper {

    public static UserDto toUserDto(User user){
        return new UserDto(user.getId(), user.getUsername(), null);
    }

    public User postToEntity(CreateUserRequest dto) {
        return User.builder().username(dto.username()).build();
    }

    public CreateUserResponse postFromEntity(User user) {
        return new CreateUserResponse(user.getId(), user.getUsername(), user.getEvents().stream().map(EventMapper::toEventDto).toList());
    }

    public FindUserByIdResponse getByIdFromEntity(User dto) {
        return new FindUserByIdResponse(dto.getId(), dto.getUsername(), dto.getEvents().stream().map(EventMapper::toEventDto).toList());
    }

    public User updateToEntity(UpdateUserRequest dto) {
        return User.builder().username(dto.username()).id(dto.id()).build();
    }

    public UpdateUserResponse updateFromEntity(User user) {
        return new UpdateUserResponse(user.getId(), user.getUsername(), user.getEvents().stream().map(EventMapper::toEventDto).toList());
    }

    public User deleteToEntity(DeleteUserRequest dto) {
        return User.builder().id(dto.id()).build();
    }
}
