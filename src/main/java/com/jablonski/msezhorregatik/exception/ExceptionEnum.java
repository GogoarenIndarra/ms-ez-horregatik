package com.jablonski.msezhorregatik.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionEnum {
    //user exceptions
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
    USER_EXISTS(HttpStatus.CONFLICT, "User already exists"),
    //security exceptions
    BAD_CREDENTIALS(HttpStatus.FORBIDDEN, "Bad credentials"),
    REFRESH_TOKEN_EXPIRED(HttpStatus.FORBIDDEN, "Refresh token was expired"),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.FORBIDDEN, "Refresh token not found");

    private final String message;
    private final HttpStatus httpStatus;

    ExceptionEnum(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
