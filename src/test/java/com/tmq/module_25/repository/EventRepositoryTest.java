package com.tmq.module_25.repository;

import com.tmq.module_25.entity.FileEntity;
import com.tmq.module_25.entity.FileStatus;
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

@SpringBootTest
@Testcontainers
public class EventRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:18-alpine")
            .withDatabaseName("app")
      .withUsername("app")
      .withPassword("app");

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry r) {
        r.add("spring.r2dbc.url", () ->
                "r2dbc:postgresql://" + postgres.getHost() + ":" + postgres.getFirstMappedPort() + "/" + postgres.getDatabaseName());
        r.add("spring.r2dbc.username", postgres::getUsername);
        r.add("spring.r2dbc.password", postgres::getPassword);

        r.add("spring.flyway.url", postgres::getJdbcUrl);
        r.add("spring.flyway.user", postgres::getUsername);
        r.add("spring.flyway.password", postgres::getPassword);
        r.add("spring.flyway.locations", () -> "classpath:migration");
        r.add("spring.flyway.enabled", () -> "true");
    }

    @Autowired
    FileRepository fileRepository;

    @Test
    void test(){
        StepVerifier.create(fileRepository.findAll())
                .expectNext(new FileEntity(1L, "report.pdf",   "s3://bucket/report.pdf",   FileStatus.ACTIVE));

    }
}
