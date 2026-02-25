package com.tmq.individuals_api.service;

import com.tmq.individuals_api.client.KeycloakClient;
import com.tmq.individuals_api.dto.TokenResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @Mock
    private KeycloakClient keycloakClient;

    @InjectMocks
    private TokenService tokenService;

    @Test
    void getAccessToken_success() {
        TokenResponse expected = new TokenResponse()
                .accessToken("access-token")
                .refreshToken("refresh-token");
        when(keycloakClient.getUserToken("user@example.com", "password123"))
                .thenReturn(Mono.just(expected));

        StepVerifier.create(tokenService.getAccessToken("user@example.com", "password123"))
                .expectNext(expected)
                .verifyComplete();
    }

    @Test
    void getAccessToken_clientError_propagatesError() {
        when(keycloakClient.getUserToken("user@example.com", "wrong"))
                .thenReturn(Mono.error(new RuntimeException("401 Unauthorized")));

        StepVerifier.create(tokenService.getAccessToken("user@example.com", "wrong"))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void refreshToken_success() {
        TokenResponse expected = new TokenResponse()
                .accessToken("new-access")
                .refreshToken("new-refresh");
        when(keycloakClient.refreshToken("old-refresh-token"))
                .thenReturn(Mono.just(expected));

        StepVerifier.create(tokenService.refreshToken("old-refresh-token"))
                .expectNext(expected)
                .verifyComplete();
    }

    @Test
    void refreshToken_clientError_propagatesError() {
        when(keycloakClient.refreshToken("expired-token"))
                .thenReturn(Mono.error(new RuntimeException("400 Bad Request")));

        StepVerifier.create(tokenService.refreshToken("expired-token"))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void getAdminToken_success() {
        TokenResponse expected = new TokenResponse().accessToken("admin-access-token");
        when(keycloakClient.getAdminToken()).thenReturn(Mono.just(expected));

        StepVerifier.create(tokenService.getAdminToken())
                .expectNext(expected)
                .verifyComplete();
    }

    @Test
    void getAdminToken_clientError_propagatesError() {
        when(keycloakClient.getAdminToken())
                .thenReturn(Mono.error(new RuntimeException("500 Internal Server Error")));

        StepVerifier.create(tokenService.getAdminToken())
                .expectError(RuntimeException.class)
                .verify();
    }
}