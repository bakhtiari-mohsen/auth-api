package com.efarda.wealthmanagement.authapi.security;

import com.efarda.wealthmanagement.authapi.dto.AccountInfo;
import com.efarda.wealthmanagement.common.valueobject.AccountId;
import com.efarda.wealthmanagement.common.valueobject.Mobile;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;

public record AuthorizedAccount(AccountInfo account, @Nullable Mobile mobilePhoneNumber) {
    public AccountInfo getAccount() {
        return account();
    }

    public AccountId accountId() {
        return account().getId();
    }

    public Set<GrantedAuthority> getGrantedAuthorities() {
        return Set.of(new SimpleGrantedAuthority("USER"));
        /*return account().getScopes().stream()
                .map(AccountScope::getName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());*/
    }
}
