package com.jablonski.msezhorregatik.exception;

import lombok.Getter;

@Getter
public class RestException extends RuntimeException {
    private final ExceptionEnum exceptionEnum;

    public RestException(ExceptionEnum exceptionEnum) {
        super(exceptionEnum.getMessage());
        this.exceptionEnum = exceptionEnum;
    }
}
