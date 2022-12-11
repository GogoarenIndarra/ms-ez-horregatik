package com.jablonski.msezhorregatik.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

import static java.lang.System.currentTimeMillis;

@Component
class JwtTokenUtil implements Serializable {
    @Serial
    private static final long serialVersionUID = -2550185165626007488L;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.tokenValidity}")
    private Long jwtTokenValidity;

    String generateToken(final UserDetails userDetails) {
        return Jwts.builder()
            .setClaims(new HashMap<>())
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date(currentTimeMillis()))
            .setExpiration(new Date(currentTimeMillis() + jwtTokenValidity * 3600 * 1000))
            .signWith(SignatureAlgorithm.HS512, secret)
            .compact();
    }

    Boolean validateToken(final String token, final UserDetails userDetails) {
        return getUsernameFromToken(token).equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    String getUsernameFromToken(final String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    private Boolean isTokenExpired(final String token) {
        return getClaimFromToken(token, Claims::getExpiration).before(new Date());
    }

    private <T> T getClaimFromToken(final String token, final Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(
            Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody());
    }
}
