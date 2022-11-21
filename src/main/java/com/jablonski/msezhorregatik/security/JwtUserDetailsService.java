package com.jablonski.msezhorregatik.security;

import com.jablonski.msezhorregatik.registration.UserRepository;
import com.jablonski.msezhorregatik.exception.ExceptionEnum;
import com.jablonski.msezhorregatik.exception.RestException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
class JwtUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(final String email) throws RestException {
        var user = userRepository.findByEmail(email).orElseThrow(
                () -> new RestException(ExceptionEnum.BAD_CREDENTIALS));

        return new User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }

    public UserDetails loadUserByUserId(final UUID userId) throws RestException {
        final var user = userRepository.findById(userId).orElseThrow(
                () -> new RestException(ExceptionEnum.USER_NOT_FOUND));

        return new User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }

    public com.jablonski.msezhorregatik.registration.dto.User loadByUserEmail(final String userEmail) throws RestException {

        return userRepository.findByEmail(userEmail).orElseThrow(
                () -> new RestException(ExceptionEnum.USER_NOT_FOUND));
    }
}
