package com.tmq.individuals_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tmq.individuals_api.dto.TokenRefreshRequest;
import com.tmq.individuals_api.dto.TokenResponse;
import com.tmq.individuals_api.dto.UserLoginRequest;
import com.tmq.individuals_api.dto.UserRegistrationRequest;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthRestControllerV1IntegrationTest {

    private static final String REALM = "payment-system";

    private static final String CLIENT_UUID = "e70d7d14-3756-488f-9abc-245aa788995c";

    private static final String TEST_EMAIL = "test." + UUID.randomUUID() + "@example.com";
    private static final String TEST_PASSWORD = "Test1234!";

    private static String accessToken;
    private static String refreshToken;

    @Container
    static final KeycloakContainer keycloak = new KeycloakContainer("quay.io/keycloak/keycloak:26.2")
            .withRealmImportFile("realm-config.json");

    @LocalServerPort
    private int port;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    @DynamicPropertySource
    static void configureKeycloakProperties(DynamicPropertyRegistry registry) {
        String baseUrl = keycloak.getAuthServerUrl();
        String clientSecret = regenerateClientSecret(baseUrl);

        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri",
                () -> baseUrl + "/realms/" + REALM);
        registry.add("spring.security.oauth2.client.provider.keycloak.issuer-uri",
                () -> baseUrl + "/realms/" + REALM);
        registry.add("spring.security.oauth2.client.registration.keycloak.client-secret",
                () -> clientSecret);
        registry.add("keycloak.url", () -> "http://" + keycloak.getHost());
        registry.add("keycloak.port", () -> String.valueOf(keycloak.getMappedPort(8080)));
    }

    private static String regenerateClientSecret(String keycloakBaseUrl) {
        try {
            var http = HttpClient.newHttpClient();
            var mapper = new ObjectMapper();

            String formBody = "grant_type=password&client_id=admin-cli&username=%s&password=%s"
                    .formatted(keycloak.getAdminUsername(), keycloak.getAdminPassword());

            var tokenResp = http.send(
                    HttpRequest.newBuilder()
                            .uri(URI.create(keycloakBaseUrl + "/realms/master/protocol/openid-connect/token"))
                            .header("Content-Type", "application/x-www-form-urlencoded")
                            .POST(HttpRequest.BodyPublishers.ofString(formBody))
                            .build(),
                    HttpResponse.BodyHandlers.ofString()
            );

            String adminToken = mapper.readTree(tokenResp.body()).get("access_token").asText();

            var secretResp = http.send(
                    HttpRequest.newBuilder()
                            .uri(URI.create(keycloakBaseUrl + "/admin/realms/" + REALM
                                    + "/clients/" + CLIENT_UUID + "/client-secret"))
                            .header("Authorization", "Bearer " + adminToken)
                            .POST(HttpRequest.BodyPublishers.noBody())
                            .build(),
                    HttpResponse.BodyHandlers.ofString()
            );

            return mapper.readTree(secretResp.body()).get("value").asText();
        } catch (Exception e) {
            throw new RuntimeException("Failed to regenerate Keycloak client secret", e);
        }
    }


    @Test
    @Order(1)
    void registration_newUser_returns201WithTokens() {
        TokenResponse result = webTestClient.post()
                .uri("/api/v1/auth/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserRegistrationRequest(TEST_EMAIL, TEST_PASSWORD, TEST_PASSWORD))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(TokenResponse.class)
                .returnResult()
                .getResponseBody();

        assertThat(result).isNotNull();
        assertThat(result.getAccessToken()).isNotBlank();
        assertThat(result.getRefreshToken()).isNotBlank();

        accessToken = result.getAccessToken();
        refreshToken = result.getRefreshToken();
    }

    @Test
    @Order(5)
    void registration_duplicateEmail_returns409() {
        webTestClient.post()
                .uri("/api/v1/auth/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserRegistrationRequest(TEST_EMAIL, TEST_PASSWORD, TEST_PASSWORD))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody()
                .jsonPath("$.status").isEqualTo(409);
    }

    @Test
    @Order(6)
    void registration_invalidEmailFormat_returns400() {
        webTestClient.post()
                .uri("/api/v1/auth/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserRegistrationRequest("not-an-email", TEST_PASSWORD, TEST_PASSWORD))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.status").isEqualTo(400);
    }

    @Test
    @Order(7)
    void registration_passwordMismatch_returns400() {
        webTestClient.post()
                .uri("/api/v1/auth/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserRegistrationRequest(
                        "another." + UUID.randomUUID() + "@example.com",
                        TEST_PASSWORD, "DifferentPassword!"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.status").isEqualTo(400);
    }

    @Test
    @Order(2)
    void login_validCredentials_returns200WithTokens() {
        webTestClient.post()
                .uri("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserLoginRequest(TEST_EMAIL, TEST_PASSWORD))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.access_token").isNotEmpty()
                .jsonPath("$.refresh_token").isNotEmpty();
    }

    @Test
    @Order(8)
    void login_wrongPassword_returns401() {
        webTestClient.post()
                .uri("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserLoginRequest(TEST_EMAIL, "wrong-password"))
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .jsonPath("$.status").isEqualTo(401);
    }

    @Test
    @Order(3)
    void refreshToken_validToken_returns200WithNewTokens() {
        webTestClient.post()
                .uri("/api/v1/auth/refresh-token")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new TokenRefreshRequest(refreshToken))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.access_token").isNotEmpty()
                .jsonPath("$.refresh_token").isNotEmpty();
    }

    @Test
    @Order(9)
    void refreshToken_invalidToken_returns401() {
        webTestClient.post()
                .uri("/api/v1/auth/refresh-token")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new TokenRefreshRequest("invalid.refresh.token.value"))
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .jsonPath("$.status").isEqualTo(401);
    }

    @Test
    @Order(4)
    void getUserInfo_withValidJwt_returns200WithUserData() {
        webTestClient.get()
                .uri("/api/v1/auth/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.email").isEqualTo(TEST_EMAIL)
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.created_at").isNotEmpty();
    }

    @Test
    @Order(10)
    void getUserInfo_noAuthentication_returns401() {
        webTestClient.get()
                .uri("/api/v1/auth/me")
                .exchange()
                .expectStatus().isUnauthorized();
    }
}