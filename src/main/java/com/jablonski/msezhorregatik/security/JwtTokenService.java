package com.jablonski.msezhorregatik.security;

import com.jablonski.msezhorregatik.security.dto.JwtRequest;
import com.jablonski.msezhorregatik.security.dto.JwtResponse;
import com.jablonski.msezhorregatik.security.dto.TokenRefreshRequest;

interface JwtTokenService {
    JwtResponse createToken(JwtRequest authenticationRequest);

    JwtResponse refreshToken(TokenRefreshRequest token);
}
