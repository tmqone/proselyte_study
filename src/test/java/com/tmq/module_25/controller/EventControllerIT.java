package com.tmq.module_25.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class EventControllerIT extends ControllerIntegrationTestBase {

    @Test
    void userCanReadOwnEvents() {
        String userToken = login("ivan", "ivan");

        webTestClient.get()
                .uri("/api/v1/events")
                .headers(headers -> headers.setBearerAuth(userToken))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.length()").isEqualTo(3);

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/events")
                        .queryParam("file_id", 1)
                        .build())
                .headers(headers -> headers.setBearerAuth(userToken))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.length()").isEqualTo(1);
    }

    @Test
    void adminCanCreateAndDeleteEvent() {
        String adminToken = login("admin", "admin");

        EntityExchangeResult<byte[]> createResult = webTestClient.post()
                .uri("/api/v1/events/admin")
                .headers(headers -> headers.setBearerAuth(adminToken))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of(
                        "userId", 1,
                        "user_id", 1,
                        "fileId", 1,
                        "file_id", 1,
                        "status", "CREATED"
                ))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .returnResult();

        JsonNode created = readJson(createResult);
        long eventId = created.path("id").asLong();
        assertThat(eventId).isPositive();

        webTestClient.delete()
                .uri("/api/v1/events/admin/{id}", eventId)
                .headers(headers -> headers.setBearerAuth(adminToken))
                .exchange()
                .expectStatus().isOk();
    }
}
