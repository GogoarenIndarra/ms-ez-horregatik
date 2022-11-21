package com.jablonski.msezhorregatik;

import com.jablonski.msezhorregatik.registration.dto.State;
import com.jablonski.msezhorregatik.registration.dto.User;
import com.jablonski.msezhorregatik.security.dto.JwtRequest;
import com.jablonski.msezhorregatik.security.dto.RefreshToken;
import com.jablonski.msezhorregatik.security.dto.RefreshTokenRequest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public final class AuthUtil {

    public static final ZonedDateTime NOW = ZonedDateTime.of(
            2022,
            6,
            6,
            12,
            30,
            30,
            0,
            ZoneId.of("GMT")
    );

    public static final LocalDateTime EXPIRY_DATE = LocalDateTime.of(
            2022,
            6,
            6,
            12,
            30,
            29
    );

    public static final LocalDateTime VALID_DATE = LocalDateTime.of(
            2022,
            6,
            6,
            12,
            30,
            31
    );

    public static RefreshToken mockRefreshToken(final Map<String, Object> mockedData) {
        return RefreshToken.builder()
                .id((UUID) Optional.ofNullable(mockedData.get("id"))
                        .orElse(UUID.fromString("b0482d64-d796-4672-b77e-f7fa53ec2222")))
                .user((User) Optional.ofNullable(mockedData.get("user"))
                        .orElse(mockUser(new HashMap<>())))
                .token((String) Optional.ofNullable(mockedData.get("token"))
                        .orElse("b0482d64-d796-4672-b77e-f7fa53ec2af3"))
                .expiryDate((LocalDateTime) Optional.ofNullable(mockedData.get("expDate"))
                        .orElse(VALID_DATE))
                .build();
    }

    public static RefreshTokenRequest mockTokenRefreshRequest(final String token) {
        return new RefreshTokenRequest(Optional.ofNullable(token)
                .orElse("b0482d64-d796-4672-b77e-f7fa53ec2af3"));
    }

    public static JwtRequest mockJwtRequest(final Map<String, Object> mockedData) {
        return new JwtRequest(
                (String) Optional.ofNullable(mockedData.get("username"))
                        .orElse("email@example.com"),
                (String) Optional.ofNullable(mockedData.get("password"))
                        .orElse("hardToGuess")
        );
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
