package com.jablonski.msezhorregatik.security;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtUserDetailsService jwtUserDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain chain) throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader("Authorization");
        Optional<String> username = Optional.empty();
        String jwtToken = null;
        if (isTokenStartsWIthWantedPhraseAndIsNotNull(requestTokenHeader)) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = Optional.ofNullable(jwtTokenUtil.getUsernameFromToken(jwtToken));
            } catch (IllegalArgumentException e) {
                logger.warn("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                logger.warn("JWT Token has expired");
            }
        } else {
            logger.warn("JWT Token does not begin with Bearer String");
        }
        final SecurityContext context = SecurityContextHolder.getContext();
        if (username.isPresent() && context.getAuthentication() == null) {
            final UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username.get());
            if (Boolean.TRUE.equals(jwtTokenUtil.validateToken(jwtToken, userDetails))) {
                final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        buildAuthToken(request, userDetails);
                context.setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken buildAuthToken(final HttpServletRequest request,
                                                               final UserDetails userDetails) {
        final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        usernamePasswordAuthenticationToken
                .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return usernamePasswordAuthenticationToken;
    }

    private boolean isTokenStartsWIthWantedPhraseAndIsNotNull(final String header) {
        return header != null && header.startsWith("Bearer ");
    }
}
