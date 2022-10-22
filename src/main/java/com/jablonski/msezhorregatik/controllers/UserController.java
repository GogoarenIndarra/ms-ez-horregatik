package com.jablonski.msezhorregatik.controllers;

import com.jablonski.msezhorregatik.registration.UserFacade;
import com.jablonski.msezhorregatik.registration.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
class UserController {
    private final UserFacade facade;

    @PostMapping("/registration")
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
