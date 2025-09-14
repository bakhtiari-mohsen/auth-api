package com.efarda.wealthmanagement.authapi.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.regex.Pattern;

import static java.util.Objects.isNull;


public class AccountAccessTokenAuthenticationFilter extends OncePerRequestFilter {
    private static final Pattern AUTHORIZATION_TOKEN_PATTERN =
            Pattern.compile("^Bearer (?<token>[a-zA-Z0-9-_=~+/]+[.][a-zA-Z0-9-_=~+/]+=*)$", Pattern.CASE_INSENSITIVE);
    public static final String BY_PASS_FILTERING_KEY = "security.com.efarda.wealthmanagement.authapi.AccountAccessTokenAuthenticationFilter";
    private final Set<String> headersForSearchingToken;
    private final AuthenticationManager authenticationManager;
    private final AccountAuthenticationFailureHandler.Exhaustive authenticationFailureHandler;

    private AccountAccessTokenAuthenticationFilter(
            Set<String> headersForSearchingToken,
            AuthenticationManager authenticationManager,
            AccountAuthenticationFailureHandler.Exhaustive authenticationFailureHandler) {
        this.headersForSearchingToken = headersForSearchingToken;
        this.authenticationManager = authenticationManager;
        this.authenticationFailureHandler = authenticationFailureHandler;
    }

    public static AccountAccessTokenAuthenticationFilter searchingAuthorizationHeader(
            AuthenticationManager authenticationManager,
            AccountAuthenticationFailureHandler.Exhaustive authenticationFailureHandler) {
        return new AccountAccessTokenAuthenticationFilter(
                Set.of(HttpHeaders.AUTHORIZATION), authenticationManager, authenticationFailureHandler);
    }

    public static AccountAccessTokenAuthenticationFilter searchingProvidedHeader(
            String extraHeaderForSearchingToken,
            AuthenticationManager authenticationManager,
            AccountAuthenticationFailureHandler.Exhaustive authenticationFailureHandler) {
        return new AccountAccessTokenAuthenticationFilter(
                Set.of(extraHeaderForSearchingToken), authenticationManager, authenticationFailureHandler);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        final var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (isNull(authentication)) return false;
        if (authentication instanceof AnonymousAuthenticationToken) return false;

        return authentication.isAuthenticated();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final var token = searchForToken(request);
        if (!StringUtils.hasText(token)) {
            authenticationFailureHandler.onAuthenticationFailure(request, response);
            return;
        }

        final var unauthenticated = AuthorizationHeaderAuthenticationToken.unauthenticated(token);

        try {
            final var authenticated = authenticationManager.authenticate(unauthenticated);

            SecurityContextHolder.getContext().setAuthentication(authenticated);

            filterChain.doFilter(request, response);
        } catch (AuthenticationException ignored) {
            authenticationFailureHandler.onAuthenticationFailure(request, response);
        }
    }

    private @Nullable String searchForToken(HttpServletRequest request) {
        final var headerToken = searchForTokenInHeaders(request);
        final var cookieToken = searchForTokenInCookie(request);

        if (StringUtils.hasText(headerToken)) return headerToken;

        if (StringUtils.hasText(cookieToken)) return cookieToken;

        return null;
    }

    private @Nullable String searchForTokenInHeaders(HttpServletRequest request) {
        for (final var header : headersForSearchingToken) {
            final var token = extractToken(request, header);
            if (StringUtils.hasText(token)) return token;
        }

        return null;
    }

    private @Nullable String searchForTokenInCookie(HttpServletRequest request) {
        for (final var header : headersForSearchingToken) {
            final var token = extractTokenFromCookie(request, header);
            if (StringUtils.hasText(token)) return token;
        }

        return null;
    }

    private @Nullable String extractTokenFromCookie(HttpServletRequest request, String header) {
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(header))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }

    private @Nullable String extractToken(HttpServletRequest request, String header) {
        final var authorization = request.getHeader(header);
        if (!StringUtils.startsWithIgnoreCase(authorization, "bearer")) return null;

        final var matcher = AUTHORIZATION_TOKEN_PATTERN.matcher(authorization);
        if (!matcher.matches()) return null;

        return matcher.group("token");
    }
}
