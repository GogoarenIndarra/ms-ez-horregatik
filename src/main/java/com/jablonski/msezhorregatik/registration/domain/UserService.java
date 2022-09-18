package com.jablonski.msezhorregatik.registration.domain;

import com.jablonski.msezhorregatik.registration.domain.dto.State;
import com.jablonski.msezhorregatik.registration.domain.dto.User;
import com.jablonski.msezhorregatik.registration.domain.dto.UserDTO;
import com.jablonski.msezhorregatik.registration.domain.exception.ExceptionEnum;
import com.jablonski.msezhorregatik.registration.domain.exception.RestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
class UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    private static final String USER_NOT_FOUND_FOR_ID_LOG = "User not found for id: {}";
    private static final String USER_ALREADY_EXISTS_FOR_EMAIL_LOG = "User already exists for email: {}";

    UUID createUser(final UserDTO userDTO) {
        try {
            return repository.save(mapper.toUser(userDTO, State.ACTIVE)).getId();
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
            repository.save(user);
            log.info("User {} updated successfully", userId);
        } catch (DataIntegrityViolationException e) {
            log.error(USER_ALREADY_EXISTS_FOR_EMAIL_LOG, userDTO.getEmail());
            throw new RestException(ExceptionEnum.USER_EXISTS);
        }
    }
}
