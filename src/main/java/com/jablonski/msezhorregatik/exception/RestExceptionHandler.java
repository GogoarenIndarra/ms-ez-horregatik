package com.jablonski.msezhorregatik.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class RestExceptionHandler {

    @ExceptionHandler(RestException.class)
    ResponseEntity<ExceptionResponse> restExceptionHandler(final RestException exception) {
        return new ResponseEntity<>(ExceptionResponse.builder()
            .message(exception.getMessage())
            .status(exception.getExceptionEnum().getHttpStatus().value()).build(),
            exception.getExceptionEnum().getHttpStatus());
    }
}
