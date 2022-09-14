package com.jablonski.msezhorregatik.registration.domain.exception;


import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
class ExceptionResponse {
    private int status;
    private String message;
}
