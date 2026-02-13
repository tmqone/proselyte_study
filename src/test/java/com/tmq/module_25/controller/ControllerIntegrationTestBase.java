package com.tmq.module_25.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletionException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(ControllerIntegrationTestBase.TestSecurityConfig.class)
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class ControllerIntegrationTestBase {
    protected static final String MINIO_ACCESS_KEY = "minioadmin";
    protected static final String MINIO_SECRET_KEY = "minioadmin12345";
    protected static final String MINIO_BUCKET = "back";

    @Container
    protected static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:18.1")
            .withDatabaseName("app")
            .withUsername("app")
            .withPassword("app");

    @Container
    protected static final GenericContainer<?> MINIO = new GenericContainer<>(DockerImageName.parse("minio/minio:latest"))
            .withEnv("MINIO_ROOT_USER", MINIO_ACCESS_KEY)
            .withEnv("MINIO_ROOT_PASSWORD", MINIO_SECRET_KEY)
            .withCommand("server /data --console-address :9001")
            .withExposedPorts(9000)
            .waitingFor(Wait.forHttp("/minio/health/ready").forPort(9000));

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry r) {
        r.add("spring.r2dbc.url", () ->
                "r2dbc:postgresql://" + POSTGRES.getHost() + ":" + POSTGRES.getFirstMappedPort() + "/" + POSTGRES.getDatabaseName());
        r.add("spring.r2dbc.username", POSTGRES::getUsername);
        r.add("spring.r2dbc.password", POSTGRES::getPassword);

        r.add("spring.flyway.url", POSTGRES::getJdbcUrl);
        r.add("spring.flyway.user", POSTGRES::getUsername);
        r.add("spring.flyway.password", POSTGRES::getPassword);
        r.add("spring.flyway.locations", () -> "classpath:migration");
        r.add("spring.flyway.enabled", () -> "true");
        r.add("spring.flyway.clean-disabled", () -> "false");

        r.add("aws.url", () -> "http://" + MINIO.getHost());
        r.add("aws.external_url", () -> "http://" + MINIO.getHost());
        r.add("aws.port", () -> MINIO.getMappedPort(9000));
        r.add("aws.accessKey", () -> MINIO_ACCESS_KEY);
        r.add("aws.secretKey", () -> MINIO_SECRET_KEY);
        r.add("aws.bucket", () -> MINIO_BUCKET);
    }

    @Autowired
    protected WebTestClient webTestClient;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private S3AsyncClient s3AsyncClient;

    @BeforeEach
    void resetDatabase() {
        Flyway flyway = Flyway.configure()
                .dataSource(POSTGRES.getJdbcUrl(), POSTGRES.getUsername(), POSTGRES.getPassword())
                .cleanDisabled(false)
                .locations("classpath:migration")
                .load();
        flyway.clean();
        flyway.migrate();
        ensureBucket();
    }

    protected String login(String username, String password) {
        EntityExchangeResult<byte[]> result = webTestClient.post()
                .uri("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("username", username, "password", password))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .returnResult();

        JsonNode json = readJson(result);
        String token = json.path("token").asText();
        assertThat(token).isNotBlank();
        return token;
    }

    protected HttpHeaders authHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return headers;
    }

    protected JsonNode readJson(EntityExchangeResult<byte[]> result) {
        try {
            return objectMapper.readTree(result.getResponseBody());
        } catch (IOException e) {
            throw new IllegalStateException("Failed to parse JSON response", e);
        }
    }

    private void ensureBucket() {
        try {
            s3AsyncClient.createBucket(CreateBucketRequest.builder().bucket(MINIO_BUCKET).build()).join();
        } catch (CompletionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof S3Exception s3) {
                String code = s3.awsErrorDetails().errorCode();
                if ("BucketAlreadyOwnedByYou".equals(code) || "BucketAlreadyExists".equals(code)) {
                    return;
                }
            }
            throw e;
        }
    }

    @TestConfiguration
    static class TestSecurityConfig {
        @Bean
        @Primary
        PasswordEncoder passwordEncoder() {
            return NoOpPasswordEncoder.getInstance();
        }
    }
}
