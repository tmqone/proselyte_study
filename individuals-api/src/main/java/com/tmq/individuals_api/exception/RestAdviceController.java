package com.tmq.individuals_api.exception;

import com.tmq.individuals_api.dto.ErrorResponse;
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
            errorResponse.setStatus(0);
            return Mono.just(ResponseEntity.badRequest().body(errorResponse));
        });
    }
}
