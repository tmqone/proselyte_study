package com.tmq.module_25.service;

import com.tmq.module_25.entity.UserEntity;
import com.tmq.module_25.entity.UserRole;
import com.tmq.module_25.entity.UserStatus;
import com.tmq.module_25.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void createUserEncodesPasswordAndSetsDefaults() {
        UserEntity input = UserEntity.builder()
                .username("ivan")
                .password("raw")
                .role(UserRole.ADMIN)
                .status(UserStatus.BLOCKED)
                .build();

        when(passwordEncoder.encode("raw")).thenReturn("encoded");
        when(userRepository.save(any(UserEntity.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(userService.createUser(input))
                .assertNext(saved -> {
                    assertThat(saved.getPassword()).isEqualTo("encoded");
                    assertThat(saved.getRole()).isEqualTo(UserRole.USER);
                    assertThat(saved.getStatus()).isEqualTo(UserStatus.ACTIVE);
                })
                .verifyComplete();

        ArgumentCaptor<UserEntity> captor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).save(captor.capture());
        assertThat(captor.getValue().getPassword()).isEqualTo("encoded");
        assertThat(captor.getValue().getRole()).isEqualTo(UserRole.USER);
        assertThat(captor.getValue().getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void updateUserEncodesPassword() {
        UserEntity input = UserEntity.builder()
                .id(5L)
                .username("admin")
                .password("raw")
                .role(UserRole.ADMIN)
                .status(UserStatus.ACTIVE)
                .build();

        when(passwordEncoder.encode("raw")).thenReturn("encoded");
        when(userRepository.save(any(UserEntity.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(userService.updateUser(input))
                .assertNext(saved -> {
                    assertThat(saved.getId()).isEqualTo(5L);
                    assertThat(saved.getPassword()).isEqualTo("encoded");
                    assertThat(saved.getRole()).isEqualTo(UserRole.ADMIN);
                    assertThat(saved.getStatus()).isEqualTo(UserStatus.ACTIVE);
                })
                .verifyComplete();
    }

    @Test
    void getUserByIdReturnsRepositoryValue() {
        UserEntity user = UserEntity.builder().id(1L).username("ivan").build();
        when(userRepository.findById(1L)).thenReturn(Mono.just(user));

        StepVerifier.create(userService.getUserById(1L))
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    void getUserByUsernameReturnsRepositoryValue() {
        UserEntity user = UserEntity.builder().id(2L).username("admin").build();
        when(userRepository.findByUsername("admin")).thenReturn(Mono.just(user));

        StepVerifier.create(userService.getUserByUsername("admin"))
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    void deleteUserCompletes() {
        when(userRepository.deleteById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(userService.deleteUser(1L))
                .verifyComplete();

        verify(userRepository).deleteById(1L);
    }

    @Test
    void getAllReturnsRepositoryValues() {
        UserEntity first = UserEntity.builder().id(1L).username("ivan").build();
        UserEntity second = UserEntity.builder().id(2L).username("admin").build();
        when(userRepository.findAll()).thenReturn(Flux.just(first, second));

        StepVerifier.create(userService.getAll())
                .expectNext(first, second)
                .verifyComplete();
    }
}
