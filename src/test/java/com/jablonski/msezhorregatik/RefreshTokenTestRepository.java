package com.jablonski.msezhorregatik;

import com.jablonski.msezhorregatik.security.RefreshTokenRepository;
import com.jablonski.msezhorregatik.security.dto.RefreshToken;

import java.util.Optional;
import java.util.UUID;

public class RefreshTokenTestRepository
    extends InMemoryRepository<RefreshToken, UUID>
    implements RefreshTokenRepository {

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return database.values().stream()
            .filter(t -> t.getToken().equals(token))
            .findFirst();
    }

    @Override
    public Optional<RefreshToken> findByUserId(UUID userId) {
        return database.values().stream()
            .filter(t -> t.getUser().getId().equals(userId))
            .findFirst();
    }
}
