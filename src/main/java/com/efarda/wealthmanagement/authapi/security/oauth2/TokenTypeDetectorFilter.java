package com.efarda.wealthmanagement.authapi.security.oauth2;

import com.efarda.wealthmanagement.authapi.security.AccountAccessTokenAuthenticationFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.regex.Pattern;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
final class TokenTypeDetectorFilter extends OncePerRequestFilter {
    private static final Pattern JWT_AUTHORIZATION_TOKEN_PATTERN = Pattern.compile(
            "^Bearer (?<token>[a-zA-Z0-9-_=~+/]+[.][a-zA-Z0-9-_=~+/]+[.][a-zA-Z0-9-_=~+/]+=*)$",
            Pattern.CASE_INSENSITIVE);

    private static final Pattern FARDA_AUTHORIZATION_TOKEN_PATTERN =
            Pattern.compile("^Bearer (?<token>[a-zA-Z0-9-_=~+/]+[.][a-zA-Z0-9-_=~+/]+=*)$", Pattern.CASE_INSENSITIVE);

    private final String fardaTokenHeader;

    static TokenTypeDetectorFilter putFardaTokenOnHeader(String header) {
        return new TokenTypeDetectorFilter(header);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final var authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!StringUtils.hasText(authorization)) {
            // Token not found, so pass request through other filters
            filterChain.doFilter(request, response);
            return;
        }

        final var tokenDetails = extractTokenDetails(authorization);
        switch (tokenDetails.tokenType()) {
            case JWT -> handleJwtToken(request, response, filterChain, tokenDetails.token());
            case FARDA -> handleFardaToken(request, response, filterChain, tokenDetails.token());
            case UNKNOWN -> handleUnknownToken(request, response, filterChain);
        }
    }

    private TokenDetails extractTokenDetails(String authorization) {
        // Check for JWT pattern
        final var jwtMatcher = JWT_AUTHORIZATION_TOKEN_PATTERN.matcher(authorization);
        if (jwtMatcher.matches()) return new TokenDetails(TokenType.JWT, jwtMatcher.group("token"));

        // Check for Farda pattern
        final var fardaMatcher = FARDA_AUTHORIZATION_TOKEN_PATTERN.matcher(authorization);
        if (fardaMatcher.matches()) return new TokenDetails(TokenType.FARDA, fardaMatcher.group("token"));

        return new TokenDetails(TokenType.UNKNOWN, authorization);
    }

    private void handleJwtToken(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, String token)
            throws ServletException, IOException {
        // Put token to Authorization header and let BearerTokenAuthenticatorFilter do its job
        final var wrappedRequest = new HeaderModifierRequestWrapper(request);
        wrappedRequest.setAuthorizationHeader("Bearer " + token);
        byPassAccountAccessTokenAuthenticationFilter(request);

        filterChain.doFilter(wrappedRequest, response);
    }

    private void handleFardaToken(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, String token)
            throws ServletException, IOException {
        // Remove Authorization header to bypass BearerTokenAuthenticatorFilter.
        // Put token to Farda header and let AccountAccessTokenAuthenticationFilter do its job
        final var wrappedRequest = new HeaderModifierRequestWrapper(request);
        wrappedRequest.removeAuthorizationHeader();
        wrappedRequest.addHeader(fardaTokenHeader, "Bearer " + token);
        filterChain.doFilter(wrappedRequest, response);
    }

    private void handleUnknownToken(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // could not handle unknown token, so pass it through filters
        filterChain.doFilter(request, response);
    }

    private void byPassAccountAccessTokenAuthenticationFilter(HttpServletRequest request) {
        request.setAttribute(AccountAccessTokenAuthenticationFilter.BY_PASS_FILTERING_KEY, true);
    }

    private enum TokenType {
        JWT,
        FARDA,
        UNKNOWN
    }

    private record TokenDetails(TokenType tokenType, String token) {}
}
