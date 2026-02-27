package com.tmq.individuals_api.validator;


import com.tmq.individuals_api.exception.ValidationException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.regex.Pattern;

@Component
public class UserValidator {
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$");

    public Mono<Void> validateOnRegistration(String email, String password, String confirmPassword) {
        if (email == null || email.isBlank()) {
            return Mono.error(new ValidationException("Email is required"));
        }

        if (password == null || password.isBlank()) {
            return Mono.error(new ValidationException("Password is required"));
        }

        if (!Objects.equals(password, confirmPassword)) {
            return Mono.error(new ValidationException("Passwords do not match"));
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return Mono.error(new ValidationException("Email is not valid"));
        }

        return Mono.empty();
    }
}
