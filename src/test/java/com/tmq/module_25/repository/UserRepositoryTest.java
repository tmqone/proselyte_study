package com.tmq.module_25.repository;

import com.tmq.module_25.entity.UserRole;
import com.tmq.module_25.entity.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserRepositoryTest extends RepositoryConfigurationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByUsername() {
        StepVerifier.create(userRepository.findByUsername("ivan"))
                .assertNext(user -> {
                    assertThat(user.getUsername()).isEqualTo("ivan");
                    assertThat(user.getRole()).isEqualTo(UserRole.USER);
                    assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
                })
                .verifyComplete();
    }

    @Test
    void deleteByIdUpdatesStatus() {
        StepVerifier.create(userRepository.deleteById(1L).then(userRepository.findById(1L)))
                .assertNext(user -> {
                    assertThat(user.getId()).isEqualTo(1L);
                    assertThat(user.getStatus()).isEqualTo(UserStatus.BLOCKED);
                })
                .verifyComplete();
    }
}
