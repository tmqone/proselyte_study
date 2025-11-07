package com.tmq.service;

import com.tmq.dto.user.*;
import com.tmq.exception.UserNotFoundException;
import com.tmq.mapper.UserMapper;
import com.tmq.model.User;
import com.tmq.repository.UserRepository;
import com.tmq.repository.hibernate.HibernateUserRepositoryImpl;

import java.util.List;

public class UserService{
    private final UserRepository userRepository = new HibernateUserRepositoryImpl();
    private final UserMapper userMapper = new UserMapper();

    //TODO сделать валидаторы
    public CreateUserResponse save(CreateUserRequest userDto) {
        User user = userMapper.postToEntity(userDto);
        User save = userRepository.save(user);
        return userMapper.postFromEntity(save);
    }

    public UpdateUserResponse update(UpdateUserRequest userDto) {
        User user = userMapper.updateToEntity(userDto);
        User save = userRepository.update(user);
        return userMapper.updateFromEntity(save);
    }

    public boolean delete(DeleteUserRequest userDto) {
        userMapper.deleteToEntity(userDto);
        return userRepository.delete(userDto.id());
    }

    public FindUserByIdResponse findById(Integer id) {
        return userMapper.getByIdFromEntity(userRepository.findById(id).orElseThrow(UserNotFoundException::new));
    }

    public List<FindAllUserResponse> findAll() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toUserDto)
                .map(x -> new FindAllUserResponse(x.id(), x.username(), x.events()))
                .toList();
    }
}
