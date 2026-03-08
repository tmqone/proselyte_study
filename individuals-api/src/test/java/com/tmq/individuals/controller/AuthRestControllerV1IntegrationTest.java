package com.tmq.individuals.controller;

import com.tmq.individuals.dto.AddressWriteDto;
import com.tmq.individuals.dto.TokenRefreshRequest;
import com.tmq.individuals.dto.TokenResponse;
import com.tmq.individuals.dto.UserLoginRequest;
import com.tmq.individuals.dto.UserRegistrationRequest;
import com.tmq.individuals.support.KeycloakContainerSupport;
import com.tmq.individuals.support.PersonServiceWireMockSupport;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthRestControllerV1IntegrationTest extends KeycloakContainerSupport { // TODO Убрать наследование

    private static final String TEST_EMAIL = "test." + UUID.randomUUID() + "@example.com";
    private static final String TEST_PASSWORD = "Test1234!";

    @DynamicPropertySource
    static void configurePersonServiceProperties(DynamicPropertyRegistry registry) {
        registry.add("persons.name", () -> "persons");
        registry.add("persons.contextId", () -> "persons");
        registry.add("persons.url",
                () -> "http://localhost:" + PersonServiceWireMockSupport.getPort() + "/api/v1");
    }

    @AfterAll
    static void stopPersonServiceMock() {
        PersonServiceWireMockSupport.stop();
    }

    private static String accessToken;
    private static String refreshToken;

    @LocalServerPort
    private int port;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();

        PersonServiceWireMockSupport.setupDefaultStubs();
    }

    @Test
    @Order(1)
    void registration_newUser_returns201WithTokens() {
        TokenResponse result = webTestClient.post()
                .uri("/api/v1/auth/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserRegistrationRequest()
                        .firstName("Test")
                        .lastName("User")
                        .email(TEST_EMAIL)
                        .password(TEST_PASSWORD)
                        .confirmPassword(TEST_PASSWORD)
                        .passportNumber("AB123456")
                        .phoneNumber("+1234567890")
                        .address(new AddressWriteDto()))
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

        webTestClient.get()
                .uri("/api/v1/auth/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.email").isEqualTo(TEST_EMAIL);
    }

    @Test
    @Order(5)
    void registration_duplicateEmail_returns409() {
        webTestClient.post()
                .uri("/api/v1/auth/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserRegistrationRequest()
                        .firstName("Test")
                        .lastName("User")
                        .email(TEST_EMAIL)
                        .password(TEST_PASSWORD)
                        .confirmPassword(TEST_PASSWORD)
                        .passportNumber("AB123456")
                        .phoneNumber("+1234567890")
                        .address(new AddressWriteDto()))
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
                .bodyValue(new UserRegistrationRequest()
                        .firstName("Test")
                        .lastName("User")
                        .email("not-an-email")
                        .password(TEST_PASSWORD)
                        .confirmPassword(TEST_PASSWORD)
                        .passportNumber("AB123456")
                        .phoneNumber("+1234567890")
                        .address(new AddressWriteDto()))
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
                .bodyValue(new UserRegistrationRequest()
                        .firstName("Test")
                        .lastName("User")
                        .email("another." + UUID.randomUUID() + "@example.com")
                        .password(TEST_PASSWORD)
                        .confirmPassword("DifferentPassword!")
                        .passportNumber("AB123456")
                        .phoneNumber("+1234567890")
                        .address(new AddressWriteDto()))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.status").isEqualTo(400);
    }

    @Test
    @Order(2)
    void login_validCredentials_returns200WithTokens() {
        TokenResponse result = webTestClient.post()
                .uri("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserLoginRequest(TEST_EMAIL, TEST_PASSWORD))
                .exchange()
                .expectStatus().isOk()
                .expectBody(TokenResponse.class)
                .returnResult()
                .getResponseBody();

        assertThat(result).isNotNull();
        assertThat(result.getAccessToken()).isNotBlank();
        assertThat(result.getRefreshToken()).isNotBlank();

        accessToken = result.getAccessToken();
        refreshToken = result.getRefreshToken();

        webTestClient.get()
                .uri("/api/v1/auth/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + result.getAccessToken())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.email").isEqualTo(TEST_EMAIL);
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
        TokenResponse result = webTestClient.post()
                .uri("/api/v1/auth/refresh-token")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new TokenRefreshRequest(refreshToken))
                .exchange()
                .expectStatus().isOk()
                .expectBody(TokenResponse.class)
                .returnResult()
                .getResponseBody();

        assertThat(result).isNotNull();
        assertThat(result.getAccessToken()).isNotBlank();
        assertThat(result.getRefreshToken()).isNotBlank();

        webTestClient.get()
                .uri("/api/v1/auth/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + result.getAccessToken())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.email").isEqualTo(TEST_EMAIL);
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