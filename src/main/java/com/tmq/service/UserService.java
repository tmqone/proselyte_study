package com.tmq.service;

import com.tmq.dto.user.*;
import com.tmq.exception.UserNotFoundException;
import com.tmq.mapper.UserMapper;
import com.tmq.model.User;
import com.tmq.repository.UserRepository;
import com.tmq.repository.hibernate.HibernateUserRepositoryImpl;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserService{
    private static final UserService INSTANCE = new UserService();
    private static final UserRepository userRepository = HibernateUserRepositoryImpl.getInstance();
    private static final UserMapper userMapper = new UserMapper();

    public static UserService getInstance() {
        return INSTANCE;
    }

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
                .map(UserMapper::toUserWithoutEventDto)
                .map(value -> new FindAllUserResponse(value.id(), value.name()))
                .toList();
    }

    public boolean existsById(Integer id) {
        return userRepository.existsById(id);
    }
}
