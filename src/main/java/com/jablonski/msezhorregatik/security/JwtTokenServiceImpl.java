package com.jablonski.msezhorregatik.security;

import com.jablonski.msezhorregatik.exception.ExceptionEnum;
import com.jablonski.msezhorregatik.exception.RestException;
import com.jablonski.msezhorregatik.registration.dto.User;
import com.jablonski.msezhorregatik.security.dto.JwtRequest;
import com.jablonski.msezhorregatik.security.dto.JwtResponse;
import com.jablonski.msezhorregatik.security.dto.RefreshToken;
import com.jablonski.msezhorregatik.security.dto.RefreshTokenRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
class JwtTokenServiceImpl implements JwtTokenService {
    private final AuthenticationManager authenticationManager;
    private final JwtUserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private Long refreshTokenValidity;
    private final Clock clock;

    public JwtTokenServiceImpl(final AuthenticationManager authenticationManager,
                               final JwtUserDetailsService userDetailsService,
                               final JwtTokenUtil jwtTokenUtil,
                               final RefreshTokenRepository refreshTokenRepository,
                               final @Value("${jwt.refreshTokenValidity}") Long refreshTokenValidity,
                               final Clock clock) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshTokenValidity = refreshTokenValidity;
        this.clock = clock;
    }

    @Override
    public JwtResponse createToken(final JwtRequest jwtRequest) {
        authenticate(jwtRequest.username(), jwtRequest.password());
        final UserDetails userDetails = userDetailsService
            .loadUserByUsername(jwtRequest.username());
        final String token = jwtTokenUtil.generateToken(userDetails);
        final String refreshToken = createRefreshToken(userDetails.getUsername()).getToken();

        return new JwtResponse(token, refreshToken);
    }

    @Override
    public JwtResponse refreshToken(final RefreshTokenRequest tokenRequest) {
        final String requestRefreshToken = tokenRequest.refreshToken();
        final RefreshToken refreshToken = refreshTokenRepository.findByToken(requestRefreshToken)
            .orElseThrow(() -> {
                throw new RestException(ExceptionEnum.REFRESH_TOKEN_NOT_FOUND);
            });
        verifyExpiration(refreshToken);
        final UserDetails userDetails = userDetailsService.loadUserByUserId(refreshToken.getUser().getId());
        final String token = jwtTokenUtil.generateToken(userDetails);
        final String newRefreshToken = createRefreshToken(userDetails.getUsername()).getToken();

        return new JwtResponse(token, newRefreshToken);
    }

    private RefreshToken createRefreshToken(final String userEmail) {
        final User user = userDetailsService.loadByUserEmail(userEmail);
        refreshTokenRepository.findByUserId(user.getId())
            .ifPresent(refreshToken -> refreshTokenRepository.deleteById(refreshToken.getId()));

        return refreshTokenRepository.save(
            RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .expiryDate(LocalDateTime.now(clock).plus(refreshTokenValidity, ChronoUnit.MINUTES))
                .user(user)
                .build());
    }

    private void verifyExpiration(final RefreshToken token) {
        if (token.getExpiryDate().compareTo(LocalDateTime.now(clock)) < 0) {
            refreshTokenRepository.delete(token);
            throw new RestException(ExceptionEnum.REFRESH_TOKEN_EXPIRED);
        }
    }

    private void authenticate(final String username, final String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (InternalAuthenticationServiceException e) {
            throw new RestException(ExceptionEnum.BAD_CREDENTIALS);
        }
    }
}
