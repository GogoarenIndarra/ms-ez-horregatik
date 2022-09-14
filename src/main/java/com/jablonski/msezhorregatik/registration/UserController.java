package com.jablonski.msezhorregatik.registration;

import com.jablonski.msezhorregatik.registration.domain.UserFacade;
import com.jablonski.msezhorregatik.registration.domain.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserFacade facade;

    @PostMapping("/user")
    public UUID createUser(@RequestBody final UserDTO userDTO) {
        return facade.createUser(userDTO);
    }

    @GetMapping("/user/{userId}")
    public UserDTO getUser(@PathVariable final UUID userId) {
        return facade.getUser(userId);
    }
}
