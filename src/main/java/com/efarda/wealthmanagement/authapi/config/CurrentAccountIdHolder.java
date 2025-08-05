package com.efarda.wealthmanagement.authapi.config;

import com.efarda.wealthmanagement.authapi.security.AuthorizedAccountAuthenticationToken;
import com.efarda.wealthmanagement.common.valueobject.AccountId;
import jakarta.annotation.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static java.util.Objects.isNull;

public class CurrentAccountIdHolder {

    public static @Nullable AccountId getCurrentAccountId(Authentication authentication) {
        if (authentication instanceof AuthorizedAccountAuthenticationToken token)
            return token.getAuthorizedAccount().accountId();

        return null;
    }

    public static @Nullable AccountId getCurrentAccountId() {
        final var context = SecurityContextHolder.getContext();
        if (isNull(context)) return null;

        final var authentication = context.getAuthentication();
        return getCurrentAccountId(authentication);
    }
}
