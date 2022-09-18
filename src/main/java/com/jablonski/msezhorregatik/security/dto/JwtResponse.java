package com.jablonski.msezhorregatik.security.dto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public record JwtResponse(String jwtToken, String refreshToken) {
}
