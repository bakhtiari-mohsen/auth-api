package com.efarda.wealthmanagement.authapi.security;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Collections;

@Getter
public final class AuthorizationHeaderAuthenticationToken extends AbstractAuthenticationToken {
    private final String token;

    private AuthorizationHeaderAuthenticationToken(String token, boolean authenticated) {
        super(Collections.emptyList());
        this.token = token;
        setAuthenticated(authenticated);
    }

    public static AuthorizationHeaderAuthenticationToken unauthenticated(String token) {
        return new AuthorizationHeaderAuthenticationToken(token, false);
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }
}
