package com.tmq.module_25.controller;

import com.tmq.module_25.dto.UserDto;
import com.tmq.module_25.dto.UserRequestDto;
import com.tmq.module_25.entity.UserEntity;
import com.tmq.module_25.entity.UserStatus;
import com.tmq.module_25.mapper.UserMapper;
import com.tmq.module_25.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/users/admin")
public class UserRestControllerV1 {
    private final UserService userService;
    private final UserMapper userMapper;


    @PostMapping
    public Mono<UserDto> createUser(@RequestBody UserRequestDto dto) {
        return userService.createUser(UserEntity.builder()
                        .role(dto.getRole())
                        .username(dto.getUsername())
                        .password(dto.getPassword())
                        .status(UserStatus.valueOf(dto.getStatus()))
                        .build())
                .map(userMapper::map);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteUser(@PathVariable("id") Long userId) {
        return userService.deleteUser(userId);
    }

    @PutMapping
    public Mono<UserDto> updateUser(@RequestBody UserDto userDto) {
        return userService
                .updateUser(userMapper.map(userDto))
                .map(userMapper::map);
    }

    @GetMapping("/{id}")
    public Mono<UserDto> getUser(@PathVariable("id") Long userId) {
        return userService.getUserById(userId).map(userMapper::map);
    }

    @GetMapping
    public Flux<UserDto> getAllUsers() {
        return userService.getAll().map(userMapper::map);
    }
}
