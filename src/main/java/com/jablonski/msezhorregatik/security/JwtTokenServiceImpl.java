package com.jablonski.msezhorregatik.security;

import com.jablonski.msezhorregatik.registration.domain.dto.User;
import com.jablonski.msezhorregatik.registration.domain.exception.ExceptionEnum;
import com.jablonski.msezhorregatik.registration.domain.exception.RestException;
import com.jablonski.msezhorregatik.security.dto.JwtRequest;
import com.jablonski.msezhorregatik.security.dto.JwtResponse;
import com.jablonski.msezhorregatik.security.dto.RefreshToken;
import com.jablonski.msezhorregatik.security.dto.TokenRefreshRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
class JwtTokenServiceImpl implements JwtTokenService {
    private final AuthenticationManager authenticationManager;
    private final JwtUserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    @Value("${jwt.refreshTokenValidity}")
    private Long refreshTokenValidity;

    @Override
    public JwtResponse createToken(final JwtRequest authenticationRequest) {
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);
        final String refreshToken = createRefreshToken(userDetails.getUsername()).getToken();

        return new JwtResponse(token, refreshToken);
    }

    @Override
    public JwtResponse refreshToken(final TokenRefreshRequest tokenRequest) {
        final String requestRefreshToken = tokenRequest.getRefreshToken();
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
                        .expiryDate(LocalDateTime.now().plus(refreshTokenValidity, ChronoUnit.MINUTES))
                        .user(user)
                        .build());
    }

    private void verifyExpiration(final RefreshToken token) {
        if (token.getExpiryDate().compareTo(LocalDateTime.now()) < 0) {
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
