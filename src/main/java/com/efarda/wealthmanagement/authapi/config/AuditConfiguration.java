package com.efarda.wealthmanagement.authapi.config;

import com.efarda.wealthmanagement.common.valueobject.AccountId;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditConfiguration implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        final var accountId = CurrentAccountIdHolder.getCurrentAccountId();
        return Optional.ofNullable(accountId).map(AccountId::toString);
    }
}
