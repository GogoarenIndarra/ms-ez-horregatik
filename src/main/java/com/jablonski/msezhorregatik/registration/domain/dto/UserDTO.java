package com.jablonski.msezhorregatik.registration.domain.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class UserDTO {

    private UUID id;
    private String email;
    private String password;
}
