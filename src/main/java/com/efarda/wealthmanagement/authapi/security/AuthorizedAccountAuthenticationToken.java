package com.efarda.wealthmanagement.authapi.security;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

@Getter
public final class AuthorizedAccountAuthenticationToken extends AbstractAuthenticationToken {
    private final AuthorizedAccount authorizedAccount;

    private AuthorizedAccountAuthenticationToken(
            AuthorizedAccount authorizedAccount, boolean authenticated) {
        super(authorizedAccount.getGrantedAuthorities());
        this.authorizedAccount = authorizedAccount;
        setAuthenticated(authenticated);
    }

    public static AuthorizedAccountAuthenticationToken authenticated(AuthorizedAccount authorizedAccount) {
        return new AuthorizedAccountAuthenticationToken(authorizedAccount, true);
    }

    @Override
    public Object getCredentials() {
        return authorizedAccount.accountId();
    }

    @Override
    public Object getPrincipal() {
        return authorizedAccount.account();
    }

}
