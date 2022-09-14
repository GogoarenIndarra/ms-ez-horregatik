package com.jablonski.msezhorregatik.registration.domain.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionEnum {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
    USER_EXISTS(HttpStatus.CONFLICT, "User already exists");

    private final String message;
    private final HttpStatus httpStatus;

    ExceptionEnum(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
