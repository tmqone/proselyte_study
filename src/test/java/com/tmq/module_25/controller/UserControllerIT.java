package com.tmq.module_25.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class UserControllerIT extends ControllerIntegrationTestBase {

    @Test
    void adminCrudUsers() {
        String adminToken = login("admin", "admin");

        EntityExchangeResult<byte[]> createResult = webTestClient.post()
                .uri("/api/v1/users/admin")
                .headers(headers -> headers.setBearerAuth(adminToken))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of(
                        "username", "user_it",
                        "password", "pass",
                        "role", "USER",
                        "status", "ACTIVE"
                ))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .returnResult();

        JsonNode created = readJson(createResult);
        long userId = created.path("id").asLong();
        assertThat(userId).isPositive();

        webTestClient.get()
                .uri("/api/v1/users/admin/{id}", userId)
                .headers(headers -> headers.setBearerAuth(adminToken))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo((int) userId)
                .jsonPath("$.username").isEqualTo("user_it");

        webTestClient.put()
                .uri("/api/v1/users/admin")
                .headers(headers -> headers.setBearerAuth(adminToken))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of(
                        "id", userId,
                        "username", "user_it_updated",
                        "password", "new_pass",
                        "role", "USER",
                        "status", "ACTIVE"
                ))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.username").isEqualTo("user_it_updated");

        webTestClient.delete()
                .uri("/api/v1/users/admin/{id}", userId)
                .headers(headers -> headers.setBearerAuth(adminToken))
                .exchange()
                .expectStatus().isOk();
    }
}
