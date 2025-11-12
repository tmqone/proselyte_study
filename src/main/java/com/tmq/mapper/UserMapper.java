package com.tmq.mapper;

import com.tmq.dto.entity.UserDto;
import com.tmq.dto.entity.UserWithoutEventDto;
import com.tmq.dto.user.*;
import com.tmq.model.User;

import java.util.List;

public class UserMapper {

    public static UserDto toUserDto(User user){
        return new UserDto(user.getId(), user.getUsername());
    }

    public FindAllUserResponse toFindAllUserResponse(User user){
        return new FindAllUserResponse(user.getId(), user.getUsername(), user.getRole());
    }

    public static UserWithoutEventDto toUserWithoutEventDto(User user){
        return new UserWithoutEventDto(user.getId(), user.getUsername());
    }

    public FindUserWithAllFields entityToUserDtoWithPassword(User user) {
        return new FindUserWithAllFields(user.getId(), user.getUsername(),  user.getPassword(),  user.getRole());
    }

    public User postToEntity(CreateUserRequest dto) {
        return User.builder().username(dto.username()).password(dto.password()).role(dto.role()).build();
    }

    public CreateUserResponse postFromEntity(User user) {
        return new CreateUserResponse(user.getId(), user.getUsername());
    }

    public FindUserByIdResponse getByIdFromEntity(User dto) {
        return new FindUserByIdResponse(dto.getId(), dto.getUsername(), dto.getRole());
    }

    public User updateToEntity(UpdateUserRequest dto) {
        return User.builder().username(dto.username()).id(dto.id()).role(dto.role()).build();
    }

    public UpdateUserResponse updateFromEntity(User dto) {
        return new UpdateUserResponse(dto.getId(), dto.getUsername());
    }

    public User deleteToEntity(DeleteUserRequest dto) {
        return User.builder().id(dto.id()).build();
    }
}
