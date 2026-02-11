package com.tmq.module_25.repository;

import com.tmq.module_25.entity.FileEntity;
import com.tmq.module_25.entity.FileStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;

@SpringBootTest
@Testcontainers
public class FileRepositoryTest extends RepositoryConfigurationTest {

    @Autowired
    private FileRepository fileRepository;

    @Test
    void getAllFilesTest() {
        StepVerifier.create(fileRepository.findAll())
                .expectNext(new FileEntity(1L, "1.pdf", "1", FileStatus.ACTIVE))
                .expectNext(new FileEntity(2L, "2.png", "2", FileStatus.ACTIVE))
                .expectNext(new FileEntity(3L, "3.zip", "3", FileStatus.ARCHIVED))
                .expectNext(new FileEntity(4L, "4.txt", "4", FileStatus.ACTIVE))
                .verifyComplete();
    }

    @Test
    void findFileWithUserIdTest() {
        StepVerifier.create(fileRepository.findFileWithUserId(1L, 1L))
                .expectNext(new FileEntity(1L, "1.pdf", "1", FileStatus.ACTIVE))
                .verifyComplete();
    }

    @Test
    void deleteByIdTest() {
        StepVerifier.create(fileRepository.deleteById(1L)).verifyComplete();
        StepVerifier.create(fileRepository.findById(1L))
                .expectNext(new FileEntity(1L, "1.pdf", "1", FileStatus.ARCHIVED))
                .verifyComplete();
    }

    @Test
    void findAllByUserId() {
        StepVerifier.create(fileRepository.findAllByUserId(1L))
                .expectNextCount(2L)
                .verifyComplete();
    }
}
