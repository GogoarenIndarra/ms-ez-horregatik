package com.jablonski.msezhorregatik.registration.domain;

import com.jablonski.msezhorregatik.registration.domain.dto.State;
import com.jablonski.msezhorregatik.registration.domain.dto.User;
import com.jablonski.msezhorregatik.registration.domain.dto.UserDTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
interface UserMapper {

    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "modificationDate", ignore = true)
    User toUser(final UserDTO userDTO, final State state);

    @Mapping(target = "user.password", ignore = true)
    UserDTO toDto(final User user);

    @Mapping(target = "user.creationDate", ignore = true)
    @Mapping(target = "user.modificationDate", ignore = true)
    void updateUser(@MappingTarget final User user, final UserDTO userDTO, final State state);
}
