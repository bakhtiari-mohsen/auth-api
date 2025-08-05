package com.efarda.wealthmanagement.authapi.authorization;

import com.efarda.wealthmanagement.authapi.feign.account.AccountInfoClient;
import com.efarda.wealthmanagement.authapi.feign.accountauthorization.AccountAuthorizationClient;
import com.efarda.wealthmanagement.authapi.security.AuthorizationHeaderAuthenticationToken;
import com.efarda.wealthmanagement.authapi.security.AuthorizedAccount;
import com.efarda.wealthmanagement.authapi.security.AuthorizedAccountAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

@Component
@RequiredArgsConstructor
public final class AuthorizedAccountAuthenticationProvider implements AuthenticationProvider {
    private final AccountInfoClient accountInfoClient;
    private final AccountAuthorizationClient accountAuthorizationClient;

    @Override
    public @Nullable Authentication authenticate(@Nullable Authentication authentication)
            throws AuthenticationException {
        if (authentication == null) return null;

        final var authenticationToken = (AuthorizationHeaderAuthenticationToken) authentication;
        final var token = authenticationToken.getToken();
        final var accountId = accountAuthorizationClient.verify(token).getBody();
        if (accountId == null)
            throw new BadCredentialsException("Provided accessToken (Authorization header) is not valid");

        final var account = accountInfoClient.info(accountId);

        if (isNull(account)) throw new UsernameNotFoundException("Account not found: %s".formatted(accountId));
        if (!account.isActive()) throw new LockedException("Account is inactive: %s".formatted(accountId));

        final var authorizedAccount = new AuthorizedAccount(account, null);

        return AuthorizedAccountAuthenticationToken.authenticated(authorizedAccount);
    }



    @Override
    public boolean supports(Class<?> authentication) {
        return AuthorizationHeaderAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
