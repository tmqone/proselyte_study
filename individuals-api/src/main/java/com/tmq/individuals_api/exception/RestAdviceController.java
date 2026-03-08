package com.tmq.individuals_api.exception;

import com.tmq.individuals.dto.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class RestAdviceController {
    @ExceptionHandler(ApiException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleApiException(ApiException e){
        return Mono.defer(() -> {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setError(e.getMessage());
            errorResponse.setStatus(500);
            return Mono.just(ResponseEntity.internalServerError().body(errorResponse));
        });
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleWebExchangeBindException(WebExchangeBindException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining(", "));
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError(message);
        errorResponse.setStatus(400);
        return Mono.just(ResponseEntity.badRequest().body(errorResponse));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleConstraintViolationException(ConstraintViolationException e) {
        String message = e.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collect(Collectors.joining(", "));
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError(message);
        errorResponse.setStatus(400);
        return Mono.just(ResponseEntity.badRequest().body(errorResponse));
    }

    @ExceptionHandler(ValidationException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleValidationException(ValidationException e) {
        return Mono.defer(() -> {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setError(e.getMessage());
            errorResponse.setStatus(400);
            return Mono.just(ResponseEntity.badRequest().body(errorResponse));
        });
    }

    @ExceptionHandler(KeycloakException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleKeycloakException(KeycloakException e) {
        return Mono.defer(() -> {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setError(e.getMessage());

            if (e.getMessage().contains("User exists with same email")){
                errorResponse.setStatus(409);
                return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse));
            }

            if (e.getMessage().contains("Invalid user credentials") ||
                    e.getMessage().contains("Invalid refresh token")) {
                errorResponse.setStatus(401);
                return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse));
            }

            errorResponse.setStatus(500);
            return Mono.just(ResponseEntity.internalServerError().body(errorResponse));
        });
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ErrorResponse>> handleException(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError(e.getMessage());
        errorResponse.setStatus(500);
        log.error(e.getMessage(), e);
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse));
    }
}
