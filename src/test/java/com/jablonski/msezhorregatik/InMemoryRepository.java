package com.jablonski.msezhorregatik;

import lombok.SneakyThrows;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.FluentQuery;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class InMemoryRepository<T, ID extends Serializable> implements JpaRepository<T, ID> {

    protected Map<ID, T> database = new HashMap<>();

    @Override
    public <S extends T> S save(S entity) {
        final ID id = (ID) getId(entity);

        if (findById(id).isPresent()) {
            throw new DataIntegrityViolationException("inserting duplicating entry with id: " + id);
        }

        database.put(id, entity);
        return entity;
    }

    @SneakyThrows
    private static Object getId(Object t) {
        return MethodUtils.invokeMethod(t, "getId");
    }

    @Override
    public Optional<T> findById(ID id) {
        return Optional.ofNullable(database.get(id));
    }

    @Override
    public List<T> findAll() {
        return database.values().stream().toList();
    }

    @Override
    public void deleteAll() {
        database.clear();
    }

    @Override
    public void deleteById(ID id) {
        database.remove(id);
    }

    @Override
    public void delete(T entity) {
        final ID id = (ID) getId(entity);
        database.remove(id);
    }

    @Override
    public List<T> findAll(Sort sort) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public List<T> findAllById(Iterable<ID> ids) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public long count() {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public void deleteAllById(Iterable<? extends ID> ids) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public void deleteAll(Iterable<? extends T> entities) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public <S extends T> List<S> saveAll(Iterable<S> entities) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public boolean existsById(ID id) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public void flush() {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public <S extends T> S saveAndFlush(S entity) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public <S extends T> List<S> saveAllAndFlush(Iterable<S> entities) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public void deleteAllInBatch(Iterable<T> entities) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public void deleteAllByIdInBatch(Iterable<ID> ids) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public void deleteAllInBatch() {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public T getOne(ID id) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public T getById(ID id) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public T getReferenceById(ID id) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public <S extends T> Optional<S> findOne(Example<S> example) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public <S extends T> List<S> findAll(Example<S> example) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public <S extends T> List<S> findAll(Example<S> example, Sort sort) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public <S extends T> Page<S> findAll(Example<S> example, Pageable pageable) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public <S extends T> long count(Example<S> example) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public <S extends T> boolean exists(Example<S> example) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public <S extends T, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        throw new RuntimeException("Not Implemented");
    }
}
