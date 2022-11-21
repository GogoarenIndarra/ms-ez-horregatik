package com.jablonski.msezhorregatik.exception;


import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
class ExceptionResponse {
    private int status;
    private String message;
}
