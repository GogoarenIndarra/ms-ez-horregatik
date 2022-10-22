package com.jablonski.msezhorregatik.registration;

import com.jablonski.msezhorregatik.registration.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserFacade {
    private final UserService userService;

    public UUID createUser(final UserDTO userDTO) {
        return userService.createUser(userDTO);
    }

    public UserDTO getUser(final UUID userId) {
        return userService.getUser(userId);
    }

    public void updateUser(final UUID userId, final UserDTO userDTO) {
        userService.updateUser(userId, userDTO);
    }
}
