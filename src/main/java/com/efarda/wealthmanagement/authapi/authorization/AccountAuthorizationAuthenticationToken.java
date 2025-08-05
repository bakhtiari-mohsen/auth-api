package com.efarda.wealthmanagement.authapi.authorization;

import com.efarda.wealthmanagement.authapi.dto.AccountAuthorizationDTO;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Collections;

@Getter
final class AccountAuthorizationAuthenticationToken extends AbstractAuthenticationToken {
    private final AccountAuthorizationDTO accountAuthorization;

    private AccountAuthorizationAuthenticationToken(AccountAuthorizationDTO accountAuthorization, boolean authenticated) {
        super(Collections.emptyList());
        this.accountAuthorization = accountAuthorization;
        setAuthenticated(authenticated);
    }

    static AccountAuthorizationAuthenticationToken unauthenticated(AccountAuthorizationDTO accountAuthorization) {
        return new AccountAuthorizationAuthenticationToken(accountAuthorization, false);
    }

    @Override
    public Object getCredentials() {
        return accountAuthorization;
    }

    @Override
    public Object getPrincipal() {
        return accountAuthorization;
    }

    public String getSubject() {
        return accountAuthorization.getAccessToken();
    }
}
