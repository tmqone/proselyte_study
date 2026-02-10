package com.tmq.module_25.service;

import com.tmq.module_25.entity.UserEntity;
import com.tmq.module_25.entity.UserRole;
import com.tmq.module_25.entity.UserStatus;
import com.tmq.module_25.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Mono<UserEntity> register(UserEntity user) {
        return userRepository.save(user.toBuilder()
                        .password(passwordEncoder.encode(user.getPassword()))
                        .role(UserRole.USER)
                        .status(UserStatus.ACTIVE)
                .build())
                .doOnSuccess(entity -> {
                    log.info("Registered new user: {}", entity);
                });
    }

    public Mono<UserEntity> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Mono<UserEntity> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Mono<Void> deleteUser(Long id ) {
        return userRepository.deleteById(id);
    }

    public Mono<UserEntity> updateUser(UserEntity user) {
        return userRepository.save(user.toBuilder()
                .password(passwordEncoder.encode(user.getPassword()))
                .build());
    }

    public Flux<UserEntity> getAll() {
        return userRepository.findAll();
    }
}
