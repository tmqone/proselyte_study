package com.tmq.service;

import com.tmq.dto.user.*;
import com.tmq.exception.UserNotFoundException;
import com.tmq.mapper.UserMapper;
import com.tmq.model.User;
import com.tmq.repository.UserRepository;
import com.tmq.repository.hibernate.HibernateUserRepositoryImpl;

import com.tmq.util.BCryptUtil;
import com.tmq.util.HibernateUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserService {
    private static final UserService INSTANCE = new UserService();
    private static final UserRepository userRepository = HibernateUserRepositoryImpl.getInstance();
    private static final UserMapper userMapper = new UserMapper();

    public static UserService getInstance() {
        return INSTANCE;
    }

    public CreateUserResponse save(CreateUserRequest userDto) {
        return HibernateUtil.handleRequest(() -> {
            User user = userMapper.postToEntity(userDto);
            user.setPassword(BCryptUtil.encryptPassword(user.getPassword()));
            User save = userRepository.save(user);
            return userMapper.postFromEntity(save);
        });
    }

    public UpdateUserResponse update(UpdateUserRequest userDto) {
        return HibernateUtil.handleRequest(() -> {
            User user = userMapper.updateToEntity(userDto);
            User save = userRepository.update(user);
            return userMapper.updateFromEntity(save);
        });
    }

    public boolean delete(Integer userId) {
        return HibernateUtil.handleRequest(() -> {
            if (userRepository.delete(userId)) return true;
            throw new UserNotFoundException();
        });
    }

    public FindUserByIdResponse findById(Integer id) {
        return HibernateUtil.handleRequest(() ->
                userMapper.getByIdFromEntity(userRepository.findById(id).orElseThrow(UserNotFoundException::new)));
    }

    public List<FindAllUserResponse> findAll() {
        return HibernateUtil.handleRequest(() -> {
            return userRepository.findAll()
                    .stream()
                    .map(userMapper::toFindAllUserResponse)
                    .toList();
        });
    }

    public FindUserWithAllFields findByNameWithPassword(String name) {
        return HibernateUtil.handleRequest(() -> userRepository.findByUsername(name)
                .map(userMapper::entityToUserDtoWithPassword)
                .orElseThrow(UserNotFoundException::new));
    }

    public FindUserWithAllFields findByIdWithPassword(Integer id) {
        return HibernateUtil.handleRequest(() -> userRepository.findById(id)
                .map(userMapper::entityToUserDtoWithPassword)
                .orElseThrow(UserNotFoundException::new));
    }

    public boolean isUserExistByUsername(String username) {
        return HibernateUtil.handleRequest(() -> userRepository.findByUsername(username).isPresent());
    }
}
