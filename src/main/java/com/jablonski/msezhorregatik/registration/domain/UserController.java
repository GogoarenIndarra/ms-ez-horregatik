package com.jablonski.msezhorregatik.registration.domain;

import com.jablonski.msezhorregatik.registration.domain.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
class UserController {
    private final UserFacade facade;

    @PostMapping("/user")
    UUID createUser(@RequestBody final UserDTO userDTO) {
        return facade.createUser(userDTO);
    }

    @GetMapping("/user/{userId}")
    UserDTO getUser(@PathVariable final UUID userId) {
        return facade.getUser(userId);
    }

    @PutMapping("/user/{userId}")
    void updateUser(@PathVariable final UUID userId, @RequestBody final UserDTO userDTO) {
        facade.updateUser(userId, userDTO);
    }
}
