package com.jablonski.msezhorregatik.security;

import com.jablonski.msezhorregatik.security.dto.JwtRequest;
import com.jablonski.msezhorregatik.security.dto.JwtResponse;
import com.jablonski.msezhorregatik.security.dto.TokenRefreshRequest;

public class JwtTokenService {
    public JwtResponse createToken(JwtRequest authenticationRequest) {
        return new JwtResponse();
    }

    public JwtResponse refreshToken(TokenRefreshRequest request) {
        return new JwtResponse();
    }
}
