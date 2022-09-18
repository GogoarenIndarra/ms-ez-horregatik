package com.jablonski.msezhorregatik.registration.domain;

import com.jablonski.msezhorregatik.registration.domain.dto.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public class InMemoryUserRepository implements UserRepository {
    private final ConcurrentHashMap<UUID, User> mapId = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, User> mapEmail = new ConcurrentHashMap<>();

    @Override
    public <S extends User> S save(S entity) {
        requireNonNull(entity);
        if (entity.getId() == null) {
            entity.setId(UUID.randomUUID());
        }
        if (findByEmail(entity.getEmail()).isPresent()) {
            throw new DataIntegrityViolationException("msg");
        }
        mapId.put(entity.getId(), entity);
        mapEmail.put(entity.getEmail(), entity);
        return entity;
    }

    @Override
    public Optional<User> findById(UUID uuid) {
        return Optional.ofNullable(mapId.get(uuid));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(mapEmail.get(email));
    }

    @Override
    public List<User> findAll() {
        return mapId.values().stream().toList();
    }

    @Override
    public List<User> findAllById(Iterable<UUID> uuids) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public List<User> findAll(Sort sort) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public long count() {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public void deleteById(UUID uuid) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public void delete(User entity) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public void deleteAllById(Iterable<? extends UUID> uuids) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public void deleteAll(Iterable<? extends User> entities) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public void deleteAll() {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public <S extends User> List<S> saveAll(Iterable<S> entities) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public boolean existsById(UUID uuid) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public void flush() {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public <S extends User> S saveAndFlush(S entity) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public <S extends User> List<S> saveAllAndFlush(Iterable<S> entities) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public void deleteAllInBatch(Iterable<User> entities) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public void deleteAllByIdInBatch(Iterable<UUID> uuids) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public void deleteAllInBatch() {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public User getOne(UUID uuid) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public User getById(UUID uuid) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public User getReferenceById(UUID uuid) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public <S extends User> Optional<S> findOne(Example<S> example) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public <S extends User> List<S> findAll(Example<S> example) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public <S extends User> List<S> findAll(Example<S> example, Sort sort) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public <S extends User> Page<S> findAll(Example<S> example, Pageable pageable) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public <S extends User> long count(Example<S> example) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public <S extends User> boolean exists(Example<S> example) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public <S extends User, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        throw new RuntimeException("Not Implemented");
    }
}
