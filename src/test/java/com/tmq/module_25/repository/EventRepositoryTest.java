package com.tmq.module_25.repository;

import com.tmq.module_25.entity.FileEntity;
import com.tmq.module_25.entity.FileStatus;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.internal.configuration.models.EnvironmentModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Map;
import java.util.logging.Logger;

@SpringBootTest
@Testcontainers
public class EventRepositoryTest extends RepositoryConfigurationTest{

    @Autowired
    FileRepository fileRepository;

    @Test
    void getAllFilesTest(){
        StepVerifier.create(fileRepository.findAll())
                .expectNext(new FileEntity(1L, "1.pdf",   "1",   FileStatus.ACTIVE))
                .expectNext(new FileEntity(2L, "2.png", "2", FileStatus.ACTIVE))
                .expectNext(new FileEntity(3L, "3.zip", "3", FileStatus.ARCHIVED))
                .expectNext(new FileEntity(4L, "4.txt", "4", FileStatus.ACTIVE))
                .verifyComplete();
    }

    @Test
    void findFileWithUserIdTest(){
        StepVerifier.create(fileRepository.findFileWithUserId(1L, 1L))
                .expectNext(new FileEntity(1L, "1.pdf", "1", FileStatus.ACTIVE))
                .verifyComplete();
    }

    @Test
    void deleteByIdTest(){
        StepVerifier.create(fileRepository.deleteById(1L)).verifyComplete();
        StepVerifier.create(fileRepository.findById(1L))
                .expectNext(new FileEntity(1L, "1.pdf", "1", FileStatus.ARCHIVED))
                .verifyComplete();
    }

    @Test
    void findAllByUserId() {
        StepVerifier.create(fileRepository.findAllByUserId(1L))
                .expectNextCount(2L) // Два документа в статусе ACTIVE
                .verifyComplete();
    }
}
