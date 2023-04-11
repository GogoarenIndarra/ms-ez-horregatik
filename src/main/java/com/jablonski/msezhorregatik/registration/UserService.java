package com.jablonski.msezhorregatik.registration;

import com.jablonski.msezhorregatik.exception.ExceptionEnum;
import com.jablonski.msezhorregatik.exception.RestException;
import com.jablonski.msezhorregatik.registration.dto.State;
import com.jablonski.msezhorregatik.registration.dto.User;
import com.jablonski.msezhorregatik.registration.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
class UserService {
    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    private static final String USER_NOT_FOUND_FOR_ID_LOG = "User not found for id: {}";
    private static final String USER_ALREADY_EXISTS_FOR_EMAIL_LOG = "User already exists for email: {}";

    UUID createUser(final UserDTO userDTO) {
        try {
            final User user = mapper.toUser(userDTO, State.ACTIVE);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return repository.save(user).getId();
        } catch (DataIntegrityViolationException e) {
            log.error(USER_ALREADY_EXISTS_FOR_EMAIL_LOG, userDTO.getEmail());
            throw new RestException(ExceptionEnum.USER_EXISTS);
        }
    }

    UserDTO getUser(final UUID userId) {
        final User user = repository.findById(userId).orElseThrow(() -> {
            log.error(USER_NOT_FOUND_FOR_ID_LOG, userId);
            throw new RestException(ExceptionEnum.USER_NOT_FOUND);
        });
        return mapper.toDto(user);
    }

    void updateUser(final UUID userId, final UserDTO userDTO) {
        log.info("Updating user: {}", userDTO);
        final User user = repository.findById(userId)
            .filter(user1 ->
                !State.INACTIVE.equals(user1.getState()))
            .orElseThrow(() -> {
                log.error(USER_NOT_FOUND_FOR_ID_LOG, userId);
                throw new RestException(ExceptionEnum.USER_NOT_FOUND);
            });

        mapper.updateUser(user, userDTO, State.ACTIVE);
        try {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            repository.save(user);
            log.info("User {} updated successfully", userId);
        } catch (DataIntegrityViolationException e) {
            log.error(USER_ALREADY_EXISTS_FOR_EMAIL_LOG, userDTO.getEmail());
            throw new RestException(ExceptionEnum.USER_EXISTS);
        }
    }
}
