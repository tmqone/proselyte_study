package com.tmq.module_25.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.web.reactive.function.BodyInserters;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

public class FileControllerIT extends ControllerIntegrationTestBase {

    @Test
    void userCanListAndGetFiles() {
        String userToken = login("ivan", "ivan");

        webTestClient.get()
                .uri("/api/v1/files")
                .headers(headers -> headers.setBearerAuth(userToken))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.length()").isEqualTo(2);

        webTestClient.get()
                .uri("/api/v1/files/{id}", 1)
                .headers(headers -> headers.setBearerAuth(userToken))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("1.pdf");
    }

    @Test
    void userCanUploadAndDeleteFile() {
        String userToken = login("ivan", "ivan");

        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file-data", new ByteArrayResource("hello".getBytes(StandardCharsets.UTF_8)) {
            @Override
            public String getFilename() {
                return "hello.txt";
            }
        }).contentType(MediaType.TEXT_PLAIN);

        EntityExchangeResult<byte[]> uploadResult = webTestClient.post()
                .uri("/api/v1/files")
                .headers(headers -> headers.setBearerAuth(userToken))
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .returnResult();

        JsonNode uploaded = readJson(uploadResult);
        long fileId = uploaded.path("id").asLong();
        assertThat(fileId).isPositive();

        webTestClient.get()
                .uri("/api/v1/files/{id}", fileId)
                .headers(headers -> headers.setBearerAuth(userToken))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("hello.txt");

        webTestClient.delete()
                .uri("/api/v1/files/{id}", fileId)
                .headers(headers -> headers.setBearerAuth(userToken))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void adminCanListAllFiles() {
        String adminToken = login("admin", "admin");

        webTestClient.get()
                .uri("/api/v1/files/admin/all")
                .headers(headers -> headers.setBearerAuth(adminToken))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isArray();
    }
}
