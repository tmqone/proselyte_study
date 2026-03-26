package com.tmq.individuals.service;

import com.tmq.individuals.dto.AddressWriteDto;
import com.tmq.person.service.dto.IndividualWriteDto;
import com.tmq.individuals.dto.TokenResponse;
import com.tmq.individuals.dto.UserInfoResponse;
import com.tmq.individuals.dto.UserLoginRequest;
import com.tmq.individuals.dto.UserRegistrationRequest;
import com.tmq.individuals.client.KeycloakClient;
import com.tmq.individuals.dto.KeycloakUserRepresentation;
import com.tmq.individuals.exception.ApiException;
import com.tmq.individuals.exception.ValidationException;
import com.tmq.individuals.mapper.IndividualWriteMapper;
import com.tmq.individuals.mapper.KeycloakUserMapper;
import com.tmq.individuals.metrics.AuthMetrics;
import com.tmq.individuals.validator.UserValidator;
import io.micrometer.observation.ObservationRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private KeycloakClient keycloakClient;

    @Mock
    private PersonService personService;

    @Mock
    private TokenService tokenService;

    @Mock
    private AuthMetrics authMetrics;

    @Mock
    private UserValidator userValidator;

    @Mock
    private KeycloakUserMapper keycloakUserMapper;

    @Mock
    private IndividualWriteMapper individualWriteMapper;

    @Spy
    private ObservationRegistry observationRegistry = ObservationRegistry.NOOP;

    @InjectMocks
    private UserService userService;

    private UserRegistrationRequest validRequest() {
        return new UserRegistrationRequest()
                .firstName("John")
                .lastName("Doe")
                .email("user@example.com")
                .password("password123")
                .confirmPassword("password123")
                .passportNumber("AB123456")
                .phoneNumber("+1234567890")
                .address(new AddressWriteDto());
    }

    @Test
    void register_validRequest_returnsUserToken() {
        UserRegistrationRequest request = validRequest();

        TokenResponse adminToken = new TokenResponse().accessToken("admin-token");
        TokenResponse userToken  = new TokenResponse().accessToken("user-token").refreshToken("refresh");

        when(userValidator.validateOnRegistration(any(), any(), any())).thenReturn(Mono.empty());
        when(individualWriteMapper.toIndividualWriteDto(any())).thenReturn(new IndividualWriteDto());
        when(personService.register(any())).thenReturn(Mono.just("user-uuid"));
        when(keycloakUserMapper.toKeycloakUser(any(), any())).thenReturn(
                KeycloakUserRepresentation.builder()
                        .email("user@example.com")
                        .emailVerified(true)
                        .enabled(true)
                        .requiredActions(List.of())
                        .credentials(List.of())
                        .build()
        );
        doNothing().when(authMetrics).recordRegistrationSuccess();
        when(tokenService.getAdminToken()).thenReturn(Mono.just(adminToken));
        when(keycloakClient.createNewUser(any(), eq("admin-token"))).thenReturn(Mono.just(new TokenResponse()));
        when(tokenService.getAccessToken("user@example.com", "password123")).thenReturn(Mono.just(userToken));

        StepVerifier.create(userService.register(request))
                .expectNext(userToken)
                .verifyComplete();
    }

    @Test
    void register_emailBlank_throwsValidationException() {
        UserRegistrationRequest request = validRequest().email("");

        when(userValidator.validateOnRegistration(any(), any(), any()))
                .thenReturn(Mono.error(new ValidationException("Email is required")));
        doNothing().when(authMetrics).recordRegistrationError(any(Throwable.class));

        StepVerifier.create(userService.register(request))
                .expectErrorSatisfies(e -> {
                    assertThat(e).isInstanceOf(ValidationException.class);
                    assertThat(e.getMessage()).contains("Email is required");
                })
                .verify();
    }

    @Test
    void register_emailNull_throwsValidationException() {
        UserRegistrationRequest request = validRequest().email(null);

        when(userValidator.validateOnRegistration(any(), any(), any()))
                .thenReturn(Mono.error(new ValidationException("Email is required")));
        doNothing().when(authMetrics).recordRegistrationError(any(Throwable.class));

        StepVerifier.create(userService.register(request))
                .expectErrorSatisfies(e -> {
                    assertThat(e).isInstanceOf(ValidationException.class);
                    assertThat(e.getMessage()).contains("Email is required");
                })
                .verify();
    }

    @Test
    void register_passwordBlank_throwsValidationException() {
        UserRegistrationRequest request = validRequest().password("").confirmPassword("");

        when(userValidator.validateOnRegistration(any(), any(), any()))
                .thenReturn(Mono.error(new ValidationException("Password is required")));
        doNothing().when(authMetrics).recordRegistrationError(any(Throwable.class));

        StepVerifier.create(userService.register(request))
                .expectErrorSatisfies(e -> {
                    assertThat(e).isInstanceOf(ValidationException.class);
                    assertThat(e.getMessage()).contains("Password is required");
                })
                .verify();
    }

    @Test
    void register_passwordsDoNotMatch_throwsValidationException() {
        UserRegistrationRequest request = validRequest().confirmPassword("different");

        when(userValidator.validateOnRegistration(any(), any(), any()))
                .thenReturn(Mono.error(new ValidationException("Passwords do not match")));
        doNothing().when(authMetrics).recordRegistrationError(any(Throwable.class));

        StepVerifier.create(userService.register(request))
                .expectErrorSatisfies(e -> {
                    assertThat(e).isInstanceOf(ValidationException.class);
                    assertThat(e.getMessage()).contains("Passwords do not match");
                })
                .verify();
    }

    @Test
    void register_invalidEmailFormat_throwsValidationException() {
        UserRegistrationRequest request = validRequest().email("not-an-email");

        when(userValidator.validateOnRegistration(any(), any(), any()))
                .thenReturn(Mono.error(new ValidationException("Email is not valid")));
        doNothing().when(authMetrics).recordRegistrationError(any(Throwable.class));

        StepVerifier.create(userService.register(request))
                .expectErrorSatisfies(e -> {
                    assertThat(e).isInstanceOf(ValidationException.class);
                    assertThat(e.getMessage()).contains("Email is not valid");
                })
                .verify();
    }

    @Test
    void login_validCredentials_returnsToken() {
        UserLoginRequest request = new UserLoginRequest("user@example.com", "password123");
        TokenResponse expected = new TokenResponse().accessToken("token").refreshToken("refresh");

        when(tokenService.getAccessToken("user@example.com", "password123"))
                .thenReturn(Mono.just(expected));
        doNothing().when(authMetrics).recordLoginSuccess();

        StepVerifier.create(userService.login(request))
                .expectNext(expected)
                .verifyComplete();
    }

    @Test
    void login_keycloakError_propagatesError() {
        UserLoginRequest request = new UserLoginRequest("user@example.com", "wrong-password");

        when(tokenService.getAccessToken("user@example.com", "wrong-password"))
                .thenReturn(Mono.error(new RuntimeException("401 Unauthorized")));
        doNothing().when(authMetrics).recordLoginError();

        StepVerifier.create(userService.login(request))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void getUserInfo_withJwtPrincipal_returnsUserInfo() {
        String createdAt = LocalDateTime.of(2024, 1, 15, 10, 30, 0).toString();

        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "RS256")
                .claim("sub", "user-id-123")
                .claim("email", "user@example.com")
                .claim("roles", List.of("USER", "ADMIN"))
                .claim("created_at", createdAt)
                .build();

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(jwt);

        StepVerifier.create(userService.getUserInfo(authentication))
                .assertNext((UserInfoResponse response) -> {
                    assertThat(response.getId()).isEqualTo("user-id-123");
                    assertThat(response.getEmail()).isEqualTo("user@example.com");
                    assertThat(response.getRoles()).containsExactly("USER", "ADMIN");
                    assertThat(response.getCreatedAt()).isNotNull();
                })
                .verifyComplete();
    }

    @Test
    void getUserInfo_withNonJwtPrincipal_throwsApiException() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn("not-a-jwt-object");

        StepVerifier.create(userService.getUserInfo(authentication))
                .expectErrorSatisfies(e -> {
                    assertThat(e).isInstanceOf(ApiException.class);
                    assertThat(e.getMessage()).contains("Invalid user principal");
                })
                .verify();
    }
}