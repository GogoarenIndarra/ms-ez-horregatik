package com.jablonski.msezhorregatik;

import com.jablonski.msezhorregatik.registration.UserRepository;
import com.jablonski.msezhorregatik.registration.dto.User;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;
import java.util.UUID;

public class UserTestRepository
    extends InMemoryRepository<User, UUID>
    implements UserRepository {

    @Override
    public Optional<User> findByEmail(String email) {
        return database.values().stream()
            .filter(e -> e.getEmail().equals(email)).findFirst();
    }

    @Override
    public User save(User user) {
        UUID id = user.getId();
        if (id == null) {
            user.setId(UUID.randomUUID());
        }
        findByEmail(user.getEmail()).ifPresent(u -> {
            if (u.getEmail().equals(user.getEmail()) && !u.getId().equals(user.getId())) {
                throw new DataIntegrityViolationException("inserting duplicating entry with email: " + user.getEmail());
            }
        });
        database.put(user.getId(), user);
        return user;
    }
}