package com.jablonski.msezhorregatik.registration;

import com.jablonski.msezhorregatik.registration.domain.dto.User;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.requireNonNull;

public class InMemoryUserRepository {
    private final ConcurrentHashMap<Object, User> map = new ConcurrentHashMap<>();

    User save(User entity) {
        requireNonNull(entity);
        entity.setId(UUID.randomUUID());
        map.put(entity.getId(), entity);
        map.put(entity.getEmail(), entity);
        return entity;
    }

    Optional<User> findById(UUID id){
        return Optional.ofNullable(map.get(id));
    }

    Optional<User> findByEmail(String email){
        return Optional.ofNullable(map.get(email));
    }

}
