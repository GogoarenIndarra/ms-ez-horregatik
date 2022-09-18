package com.jablonski.msezhorregatik.registration.domain;

import com.jablonski.msezhorregatik.registration.domain.dto.UserDTO;

import com.jablonski.msezhorregatik.registration.domain.dto.User;
import com.jablonski.msezhorregatik.registration.domain.dto.State;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;


public final class UserUtil {

    public static UserDTO mockUserDTO(final Map<String, Object> mockedData) {
        return UserDTO.builder()
                .id((UUID) Optional.ofNullable(mockedData.get("id"))
                        .orElse(UUID.fromString("b0482d64-d796-4672-b77e-f7fa53ec2af1")))
                .email((String) Optional.ofNullable(mockedData.get("email"))
                        .orElse("email@example.com"))
                .password((String) Optional.ofNullable(mockedData.get("password"))
                        .orElse("hardToGuess"))
                .build();
    }

    public static User mockUser(final Map<String, Object> mockedData) {
        return User.builder()
                .id((UUID) Optional.ofNullable(mockedData.get("id"))
                        .orElse(UUID.fromString("b0482d64-d796-4672-b77e-f7fa53ec2af1")))
                .email((String) Optional.ofNullable(mockedData.get("email"))
                        .orElse("email@example.com"))
                .password((String) Optional.ofNullable(mockedData.get("password"))
                        .orElse("hardToGuess"))
                .state((State) Optional.ofNullable(mockedData.get("state"))
                        .orElse(State.ACTIVE))
                .build();
    }
}
