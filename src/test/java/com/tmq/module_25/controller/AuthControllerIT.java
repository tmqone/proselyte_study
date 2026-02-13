package com.tmq.module_25.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.Map;

public class AuthControllerIT extends ControllerIntegrationTestBase {

    @Test
    void registerLoginAndInfo() {
        webTestClient.post()
                .uri("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of(
                        "username", "new_user",
                        "password", "secret"
                ))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isNumber()
                .jsonPath("$.username").isEqualTo("new_user");

        String token = login("new_user", "secret");

        webTestClient.get()
                .uri("/api/v1/auth/info")
                .headers(headers -> headers.setBearerAuth(token))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.username").isEqualTo("new_user");
    }
}
