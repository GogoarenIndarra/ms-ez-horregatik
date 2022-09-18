package com.jablonski.msezhorregatik.registration.domain;

import com.jablonski.msezhorregatik.registration.domain.dto.State;
import com.jablonski.msezhorregatik.registration.domain.dto.UserDTO;
import com.jablonski.msezhorregatik.registration.domain.exception.ExceptionEnum;
import com.jablonski.msezhorregatik.registration.domain.exception.RestException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserFacadeTest {
    private final UserRepository repository = new InMemoryUserRepository();
    private final UserMapper mapper = new UserMapperImpl();
    private final UserFacade facade = new UserFacade(new UserService(repository, mapper));

    @Test
    void createUserTest_returnUserId() {
        //given
        final UserDTO userDTO = UserUtil.mockUserDTO(new HashMap<>());

        //when
        facade.createUser(userDTO);

        //then
        assertThat(repository.findAll()).hasSize(1);
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
        repository.save(mapper.toUser(userDTO, State.ACTIVE));
        final var userId = userDTO.getId();

        //when
        final var user = facade.getUser(userId);

        //then
        assertThat(user.getEmail()).isEqualTo(userDTO.getEmail());
    }

    @Test
    void getUserTest_throwException_whenUserNotFound() {
        //given
        final UserDTO userDTO = UserUtil.mockUserDTO(new HashMap<>());
        repository.save(mapper.toUser(userDTO, State.ACTIVE));
        final var userId = UUID.randomUUID();

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
        final UserDTO userDTOSecond = UserUtil.mockUserDTO(new HashMap<>() {
            {
                put("email", "newEmail@example.com");
            }
        });
        repository.save(mapper.toUser(userDTOFirst, State.ACTIVE));

        //when
        facade.updateUser(userDTOSecond.getId(), userDTOSecond);

        //then
        final var foundedUser = repository.findByEmail(userDTOSecond.getEmail()).orElseThrow(() -> {
            throw new RestException(ExceptionEnum.USER_NOT_FOUND);
        });
        Assertions.assertEquals(userDTOSecond.getEmail(), foundedUser.getEmail());
    }

    @Test
    void updateUserTest_throwException_whenUserNotFound() {
        //given
        final UserDTO userDTOFirst = UserUtil.mockUserDTO(new HashMap<>());
        final var secondUserId = UUID.randomUUID();
        final UserDTO userDTOSecond = UserUtil.mockUserDTO(new HashMap<>() {
            {
                put("id", secondUserId);
            }

            {
                put("email", "newEmail@example.com");
            }
        });
        final UserDTO userDTOUpdateRequest = UserUtil.mockUserDTO(new HashMap<>() {
            {
                put("id", secondUserId);
            }

            {
                put("email", "email@example.com");
            }
        });
        repository.save(mapper.toUser(userDTOFirst, State.ACTIVE));
        repository.save(mapper.toUser(userDTOSecond, State.ACTIVE));

        //when
        final RestException exception = Assertions.assertThrows(RestException.class,
                () -> facade.updateUser(secondUserId, userDTOUpdateRequest));

        //then
        Assertions.assertEquals(ExceptionEnum.USER_EXISTS.getMessage(), exception.getMessage());
    }

    @Test
    void updateUserTest_throwException_whenUserIsNotActive() {
        //given
        final UserDTO userDTOFirst = UserUtil.mockUserDTO(new HashMap<>());
        final UUID secondUserId = userDTOFirst.getId();
        final UserDTO userDTOSecond = UserUtil.mockUserDTO(new HashMap<>() {
            {
                put("email", "newEmail@example.com");
            }
        });
        repository.save(mapper.toUser(userDTOFirst, State.INACTIVE));

        //when
        final RestException exception = Assertions.assertThrows(RestException.class,
                () -> facade.updateUser(secondUserId, userDTOSecond));

        //then
        Assertions.assertEquals(ExceptionEnum.USER_NOT_FOUND.getMessage(), exception.getMessage());
    }

}