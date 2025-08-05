package com.efarda.wealthmanagement.authapi.security;

import com.efarda.wealthmanagement.authapi.dto.AccountInfo;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

public class AuthorizationUtils {

    public static AuthorizationManager<RequestAuthorizationContext> hasWeight(int requiredWeight) {
        return (authenticationSupplier, context) -> {
            final var authentication = authenticationSupplier.get();
            if (authentication == null || !authentication.isAuthenticated()) {
                return new AuthorizationDecision(false);
            }

            final var account = (AccountInfo) authentication.getPrincipal();
            boolean decision = account.getRoleWeight() > requiredWeight;
            return new AuthorizationDecision(decision);
        };
    }
}
