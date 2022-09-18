package com.jablonski.msezhorregatik.security;

import com.jablonski.msezhorregatik.security.dto.JwtRequest;
import com.jablonski.msezhorregatik.security.dto.JwtResponse;
import com.jablonski.msezhorregatik.security.dto.TokenRefreshRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AutoController {
    private final JwtTokenService jwtTokenService;


    @PostMapping("/authenticate")
    public ResponseEntity<JwtResponse> createToken(@RequestBody final JwtRequest request) {

        return ResponseEntity.ok(jwtTokenService.createToken(request));
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<JwtResponse> refreshToken(@RequestBody final TokenRefreshRequest request) {

        return ResponseEntity.ok(jwtTokenService.refreshToken(request));
    }

    @PostMapping("/logOut")
    public ResponseEntity<?> logOut() {
        // TODO to implement later
        return ResponseEntity.ok(null);
    }
}
