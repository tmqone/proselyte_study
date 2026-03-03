package com.tmq.personapi.exception;

import com.tmq.common.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdviceController {
    @ExceptionHandler(value = CountryNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCountryNotFoundException(CountryNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError(e.getMessage());
        errorResponse.setStatus(400);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(value = UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError(e.getMessage());
        errorResponse.setStatus(400);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(value = ApiException.class)
    public ResponseEntity<ErrorResponse> handleUserApiException(ApiException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError(e.getMessage());
        errorResponse.setStatus(500);
        return ResponseEntity.internalServerError().body(errorResponse);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError(e.getMessage());
        errorResponse.setStatus(500);
        return ResponseEntity.internalServerError().body(errorResponse);
    }
}
