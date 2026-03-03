package com.tmq.individuals_api.metrics;

import com.tmq.individuals_api.exception.KeycloakException;
import com.tmq.individuals_api.exception.ValidationException;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class AuthMetrics {

    private static final String METRIC_REGISTRATIONS = "auth.registrations.total";
    private static final String METRIC_LOGINS = "auth.logins.total";

    private final MeterRegistry registry;

    private final Counter registrationSuccess;
    private final Counter registrationValidationError;
    private final Counter registrationUserAlreadyExists;
    private final Counter registrationKeycloakError;
    private final Counter registrationError;

    private final Counter loginSuccess;
    private final Counter loginError;


    public AuthMetrics(MeterRegistry registry) {
        this.registry = registry;

        this.registrationSuccess = counter(METRIC_REGISTRATIONS, "Registration attempts", "success");
        this.registrationValidationError = counter(METRIC_REGISTRATIONS, "Registration attempts", "validation_error");
        this.registrationUserAlreadyExists = counter(METRIC_REGISTRATIONS, "Registration attempts", "user_already_exists");
        this.registrationKeycloakError = counter(METRIC_REGISTRATIONS, "Registration attempts", "keycloak_error");
        this.registrationError = counter(METRIC_REGISTRATIONS, "Registration attempts", "error");

        this.loginSuccess = counter(METRIC_LOGINS, "Login attempts", "success");
        this.loginError = counter(METRIC_LOGINS, "Login attempts", "error");

    }

    public void recordRegistrationSuccess() {
        registrationSuccess.increment();
    }

    public void recordRegistrationError(Throwable e) {
        resolveRegistrationErrorCounter(e).increment();
    }

    public void recordLoginSuccess() {
        loginSuccess.increment();
    }

    public void recordLoginError() {
        loginError.increment();
    }

    private Counter resolveRegistrationErrorCounter(Throwable e) {
        if (e instanceof ValidationException) return registrationValidationError;
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