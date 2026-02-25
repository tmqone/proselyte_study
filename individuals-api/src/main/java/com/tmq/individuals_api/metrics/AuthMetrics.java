package com.tmq.individuals_api.metrics;

import com.tmq.individuals_api.exception.KeycloakException;
import com.tmq.individuals_api.exception.ValidationException;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

@Component
public class AuthMetrics {

    private static final String METRIC_REGISTRATIONS = "auth.registrations.total";
    private static final String METRIC_LOGINS        = "auth.logins.total";
    private static final String METRIC_USER_INFO     = "auth.user_info.total";

    private final MeterRegistry registry;

    private final Counter registrationSuccess;
    private final Counter registrationValidationError;
    private final Counter registrationUserAlreadyExists;
    private final Counter registrationKeycloakError;
    private final Counter registrationError;

    private final Counter loginSuccess;
    private final Counter loginError;

    private final Counter userInfoSuccess;
    private final Counter userInfoInvalidPrincipal;

    private final Timer registrationTimer;
    private final Timer loginTimer;

    public AuthMetrics(MeterRegistry registry) {
        this.registry = registry;

        this.registrationSuccess           = counter(METRIC_REGISTRATIONS, "Registration attempts", "success");
        this.registrationValidationError   = counter(METRIC_REGISTRATIONS, "Registration attempts", "validation_error");
        this.registrationUserAlreadyExists = counter(METRIC_REGISTRATIONS, "Registration attempts", "user_already_exists");
        this.registrationKeycloakError     = counter(METRIC_REGISTRATIONS, "Registration attempts", "keycloak_error");
        this.registrationError             = counter(METRIC_REGISTRATIONS, "Registration attempts", "error");

        this.loginSuccess = counter(METRIC_LOGINS, "Login attempts", "success");
        this.loginError   = counter(METRIC_LOGINS, "Login attempts", "error");

        this.userInfoSuccess          = counter(METRIC_USER_INFO, "User info requests", "success");
        this.userInfoInvalidPrincipal = counter(METRIC_USER_INFO, "User info requests", "invalid_principal");

        this.registrationTimer = Timer.builder("auth.registration.duration")
                .description("Time taken to complete user registration")
                .register(registry);
        this.loginTimer = Timer.builder("auth.login.duration")
                .description("Time taken to complete user login")
                .register(registry);
    }
    
    public Timer.Sample startRegistrationSample() {
        return Timer.start(registry);
    }

    public void stopRegistrationTimer(Timer.Sample sample) {
        sample.stop(registrationTimer);
    }

    public void recordRegistrationSuccess() {
        registrationSuccess.increment();
    }

    public void recordRegistrationError(Throwable e) {
        resolveRegistrationErrorCounter(e).increment();
    }
    
    public Timer.Sample startLoginSample() {
        return Timer.start(registry);
    }

    public void stopLoginTimer(Timer.Sample sample) {
        sample.stop(loginTimer);
    }

    public void recordLoginSuccess() {
        loginSuccess.increment();
    }

    public void recordLoginError() {
        loginError.increment();
    }
    
    public void recordUserInfoSuccess() {
        userInfoSuccess.increment();
    }

    public void recordUserInfoInvalidPrincipal() {
        userInfoInvalidPrincipal.increment();
    }
    
    private Counter resolveRegistrationErrorCounter(Throwable e) {
        if (e instanceof ValidationException)  return registrationValidationError;
        if (e instanceof KeycloakException ke) {
            if (ke.getMessage().contains("User exists")) return registrationUserAlreadyExists;
            return registrationKeycloakError;
        }
        return registrationError;
    }

    private Counter counter(String name, String description, String status) {
        return Counter.builder(name)
                .description(description)
                .tag("status", status)
                .register(registry);
    }
}