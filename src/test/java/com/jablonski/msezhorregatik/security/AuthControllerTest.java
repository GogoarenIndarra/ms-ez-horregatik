package com.jablonski.msezhorregatik.security;

import com.jablonski.msezhorregatik.AuthUtil;
import com.jablonski.msezhorregatik.RefreshTokenTestRepository;
import com.jablonski.msezhorregatik.UserTestRepository;
import com.jablonski.msezhorregatik.UserUtil;
import com.jablonski.msezhorregatik.exception.ExceptionEnum;
import com.jablonski.msezhorregatik.exception.RestException;
import com.jablonski.msezhorregatik.registration.UserRepository;
import com.jablonski.msezhorregatik.registration.dto.User;
import com.jablonski.msezhorregatik.security.dto.JwtRequest;
import com.jablonski.msezhorregatik.security.dto.JwtResponse;
import com.jablonski.msezhorregatik.security.dto.RefreshToken;
import com.jablonski.msezhorregatik.security.dto.RefreshTokenRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.time.Clock;
import java.util.HashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthControllerTest {

    private JwtTokenService service;
    private AuthenticationManager authenticationManager;
    private JwtTokenUtil jwtTokenUtil;
    private RefreshTokenRepository refreshTokenRepository;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        jwtTokenUtil = mock(JwtTokenUtil.class);
        authenticationManager = mock(AuthenticationManager.class);
        refreshTokenRepository = new RefreshTokenTestRepository();
        userRepository = new UserTestRepository();
        JwtUserDetailsService userDetailsService = new JwtUserDetailsService(userRepository);
        Long refreshTokenValidity = 4L;
        Clock clock = mock(Clock.class);
        service = new JwtTokenServiceImpl(
            authenticationManager,
            userDetailsService,
            jwtTokenUtil,
            refreshTokenRepository,
            refreshTokenValidity,
            clock);

        when(clock.getZone()).thenReturn(AuthUtil.NOW.getZone());
        when(clock.instant()).thenReturn(AuthUtil.NOW.toInstant());
    }

    @Test
    void authenticate_shouldReturnJwtResponse_whenRequestIsValid() {
        //given:
        final JwtRequest jwtRequest = AuthUtil.mockJwtRequest(new HashMap<>());
        final String newBearerToken = "newBearerToken";
        final User user = UserUtil.mockUser(new HashMap<>());
        userRepository.save(user);
        Mockito.when(jwtTokenUtil.generateToken(any())).thenReturn(newBearerToken);
        Mockito.when(authenticationManager.authenticate(any()))
            .thenReturn(new UsernamePasswordAuthenticationToken("dummy", "dummy"));

        //when:
        final var response = service.createToken(jwtRequest);
        final var refToken = refreshTokenRepository.findByUserId(user.getId()).orElseThrow(() -> {
                throw new RestException(ExceptionEnum.REFRESH_TOKEN_NOT_FOUND);
            }
        );
        final JwtResponse expectedResponse = new JwtResponse(newBearerToken, refToken.getToken());

        //then:
        Assertions.assertEquals(expectedResponse, response);
    }

    @Test
    void authenticate_shouldThrowException_whenPostBadCredentials() {
        //given:
        final JwtRequest jwtRequest = AuthUtil.mockJwtRequest(new HashMap<>());
        Mockito.doThrow(InternalAuthenticationServiceException.class)
            .when(authenticationManager).authenticate(any());

        //when
        final RestException exception = Assertions.assertThrows(RestException.class,
            () -> service.createToken(jwtRequest));

        //then
        Assertions.assertEquals(ExceptionEnum.BAD_CREDENTIALS.getMessage(), exception.getMessage());
    }

    @Test
    void authenticate_shouldThrowException_whenUserNotFoundInDbByEmail() {
        //given:
        final JwtRequest jwtRequest = AuthUtil.mockJwtRequest(new HashMap<>());
        Mockito.when(authenticationManager.authenticate(any()))
            .thenReturn(new UsernamePasswordAuthenticationToken("dummy", "dummy"));

        //when
        final RestException exception = Assertions.assertThrows(RestException.class,
            () -> service.createToken(jwtRequest));

        //then
        Assertions.assertEquals(ExceptionEnum.BAD_CREDENTIALS.getMessage(), exception.getMessage());
    }

    @Test
    void refreshToken_shouldReturnJwtResponse_withRefreshToken() {
        //given:
        final RefreshTokenRequest refreshTokenRequest = AuthUtil.mockTokenRefreshRequest(null);
        final RefreshToken refreshToken = AuthUtil.mockRefreshToken(new HashMap<>());
        final String newBearerToken = "newBearerToken";
        final User user = UserUtil.mockUser(new HashMap<>());
        refreshTokenRepository.save(refreshToken);
        userRepository.save(user);
        Mockito.when(jwtTokenUtil.generateToken(any())).thenReturn(newBearerToken);

        //when:
        final var response = service.refreshToken(refreshTokenRequest);
        final var refToken = refreshTokenRepository.findByUserId(user.getId()).orElseThrow(() -> {
                throw new RestException(ExceptionEnum.REFRESH_TOKEN_NOT_FOUND);
            }
        );
        final JwtResponse expectedResponse = new JwtResponse(newBearerToken, refToken.getToken());

        //then:
        Assertions.assertEquals(expectedResponse, response);
    }

    @Test
    void refreshToken_shouldThrowException_whenRefreshTokenNotExistInDB() {
        //given:
        final RefreshTokenRequest refreshTokenRequest = AuthUtil.mockTokenRefreshRequest(null);

        //when
        final RestException exception = Assertions.assertThrows(RestException.class,
            () -> service.refreshToken(refreshTokenRequest));

        //then
        Assertions.assertEquals(ExceptionEnum.REFRESH_TOKEN_NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    void refreshToken_shouldThrowException_whenRefreshTokenIsExpired() {
        //given:
        final RefreshTokenRequest refreshTokenRequest = AuthUtil.mockTokenRefreshRequest(null);

        final RefreshToken refreshToken = AuthUtil.mockRefreshToken(new HashMap<>() {
            {
                put("expDate", AuthUtil.EXPIRY_DATE);
            }
        });
        refreshTokenRepository.save(refreshToken);

        //when
        final RestException exception = Assertions.assertThrows(RestException.class,
            () -> service.refreshToken(refreshTokenRequest));

        //then
        Assertions.assertEquals(ExceptionEnum.REFRESH_TOKEN_EXPIRED.getMessage(), exception.getMessage());
    }

    @Test
    void refreshToken_shouldThrowException_whenUserNotFoundInDB() {
        //given:
        final RefreshTokenRequest refreshTokenRequest = AuthUtil.mockTokenRefreshRequest(null);
        final RefreshToken refreshToken = AuthUtil.mockRefreshToken(new HashMap<>());
        refreshTokenRepository.save(refreshToken);

        //when
        final RestException exception = Assertions.assertThrows(RestException.class,
            () -> service.refreshToken(refreshTokenRequest));

        //then
        Assertions.assertEquals(ExceptionEnum.USER_NOT_FOUND.getMessage(), exception.getMessage());
    }
}