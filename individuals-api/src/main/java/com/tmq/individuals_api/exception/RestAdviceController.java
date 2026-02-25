package com.tmq.individuals_api.exception;

import com.tmq.individuals_api.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class RestAdviceController {
    @ExceptionHandler(ApiException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleApiException(ApiException e){
        return Mono.defer(() -> {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setError(e.getMessage());
            return Mono.just(ResponseEntity.internalServerError().body(errorResponse));
        });
    }

    @ExceptionHandler(ValidationException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleValidationException(ValidationException e) {
        return Mono.defer(() -> {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setError(e.getMessage());
            return Mono.just(ResponseEntity.badRequest().body(errorResponse));
        });
    }

    @ExceptionHandler(KeycloakException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleKeycloakException(KeycloakException e) {
        return Mono.defer(() -> {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setError(e.getMessage());

            if (e.getMessage().contains("User exists with same email")){
                return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse));
            }

            if (e.getMessage().contains("Invalid user credentials") ||
                    e.getMessage().contains("Invalid refresh token")) {
                return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse));
            }

            return Mono.just(ResponseEntity.internalServerError().body(errorResponse));
        });
    }
}
