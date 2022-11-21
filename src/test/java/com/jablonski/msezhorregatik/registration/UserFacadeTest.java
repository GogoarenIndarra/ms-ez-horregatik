package com.jablonski.msezhorregatik.registration;

import com.jablonski.msezhorregatik.UserTestRepository;
import com.jablonski.msezhorregatik.UserUtil;
import com.jablonski.msezhorregatik.registration.dto.State;
import com.jablonski.msezhorregatik.registration.dto.User;
import com.jablonski.msezhorregatik.registration.dto.UserDTO;
import com.jablonski.msezhorregatik.exception.ExceptionEnum;
import com.jablonski.msezhorregatik.exception.RestException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class UserFacadeTest {
    private UserRepository repository;
    private UserMapper mapper;
    private PasswordEncoder passwordEncoder;
    private UserFacade facade;

    @BeforeEach
    void setUp() {
        mapper = new UserMapperImpl();
        repository = new UserTestRepository();
        passwordEncoder = new BCryptPasswordEncoder();
        facade = new UserFacade(new UserService(repository, mapper, passwordEncoder));
    }

    @Test
    void createUserTest_returnUserId() {
        //given
        final UserDTO userDTO = UserUtil.mockUserDTO(new HashMap<>());

        //when
        facade.createUser(userDTO);

        //then
        org.assertj.core.api.Assertions.assertThat(repository.findAll()).hasSize(1);
    }

    @Test
    void createUserTest_throwException_whenUserAlreadyExists() {
        //given
        final UserDTO userDTO = UserUtil.mockUserDTO(new HashMap<>());
        repository.save(mapper.toUser(userDTO, State.ACTIVE));

        //when
        final RestException exception = Assertions.assertThrows(RestException.class,
                () -> facade.createUser(userDTO));

        //then
        Assertions.assertEquals(ExceptionEnum.USER_EXISTS.getMessage(), exception.getMessage());
    }

    @Test
    void getUserTest_returnUserDto() {
        //given
        final UserDTO userDTO = UserUtil.mockUserDTO(new HashMap<>());
        final User user = repository.save(mapper.toUser(userDTO, State.ACTIVE));
        final UUID userId = user.getId();

        //when
        final UserDTO foundedUser = facade.getUser(userId);

        //then
        assertThat(foundedUser.getEmail()).isEqualTo(userDTO.getEmail());
    }

    @Test
    void getUserTest_throwException_whenUserNotFound() {
        //given
        final UserDTO userDTO = UserUtil.mockUserDTO(new HashMap<>());
        repository.save(mapper.toUser(userDTO, State.ACTIVE));
        final UUID userId = UUID.randomUUID();

        //when
        final RestException exception = Assertions.assertThrows(RestException.class,
                () -> facade.getUser(userId));

        //then
        Assertions.assertEquals(ExceptionEnum.USER_NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    void updateUserTest_shouldUpdateUser() {
        //given
        final UserDTO userDTOFirst = UserUtil.mockUserDTO(new HashMap<>());
        final User userFirst = repository.save(mapper.toUser(userDTOFirst, State.ACTIVE));
        final UserDTO userDTOSecond = UserUtil.mockUserDTO(new HashMap<>() {
            {
                put("id", userFirst.getId());
            }

            {
                put("email", "newEmail@example.com");
            }
        });

        log.info(repository.findAll().toString());

        //when
        facade.updateUser(userFirst.getId(), userDTOSecond);

        //then
        final User foundedUser = repository.findByEmail(userDTOSecond.getEmail()).orElseThrow(() -> {
            throw new RestException(ExceptionEnum.USER_NOT_FOUND);
        });
        Assertions.assertEquals(userDTOSecond.getEmail(), foundedUser.getEmail());
    }

    @Test
    void updateUserTest_throwException_whenUserNotFound() {
        //given
        final UserDTO userDTOFirst = UserUtil.mockUserDTO(new HashMap<>());
        repository.save(mapper.toUser(userDTOFirst, State.ACTIVE));
        final UserDTO userDTOSecond = UserUtil.mockUserDTO(new HashMap<>() {

            {
                put("email", "newEmail@example.com");
            }
        });
        final User userSecond = repository.save(mapper.toUser(userDTOSecond, State.ACTIVE));
        final UUID userIdSecond = userSecond.getId();
        final UserDTO userDTOUpdateRequest = UserUtil.mockUserDTO(new HashMap<>() {
            {
                put("id", userIdSecond);
            }
        });

        //when
        final RestException exception = Assertions.assertThrows(RestException.class,
                () -> facade.updateUser(userIdSecond, userDTOUpdateRequest));

        //then
        Assertions.assertEquals(ExceptionEnum.USER_EXISTS.getMessage(), exception.getMessage());
    }

    @Test
    void updateUserTest_throwException_whenUserIsNotActive() {
        //given
        final UserDTO userDTOFirst = UserUtil.mockUserDTO(new HashMap<>());
        final User userFirst = repository.save(mapper.toUser(userDTOFirst, State.INACTIVE));
        final UUID secondUserId = userFirst.getId();
        final UserDTO userDTOSecond = UserUtil.mockUserDTO(new HashMap<>() {
            {
                put("id", secondUserId);
            }

            {
                put("email", "newEmail@example.com");
            }
        });

        //when
        final RestException exception = Assertions.assertThrows(RestException.class,
                () -> facade.updateUser(secondUserId, userDTOSecond));

        //then
        Assertions.assertEquals(ExceptionEnum.USER_NOT_FOUND.getMessage(), exception.getMessage());
    }

}