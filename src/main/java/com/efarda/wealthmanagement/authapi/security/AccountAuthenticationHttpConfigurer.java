package com.efarda.wealthmanagement.authapi.security;

import com.efarda.wealthmanagement.authapi.security.oauth2.Oauth2ResourceServerIntegratedAuthenticationHttpConfigurer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@RequiredArgsConstructor
public class AccountAuthenticationHttpConfigurer
        extends AbstractHttpConfigurer<AccountAuthenticationHttpConfigurer, HttpSecurity> {
    protected final AccountAuthenticationFailureHandler.Exhaustive authenticationFailureHandler;

    public static HttpSecurity accountsShouldBeAuthenticated(
            HttpSecurity http, AccountAuthenticationFailureHandler.Exhaustive authenticationFailureHandler)
            throws Exception {
        return http.with(new AccountAuthenticationHttpConfigurer(authenticationFailureHandler), withDefaults());
    }

    public static SecurityFilterChain integrateWithOauth2ResourceServer(
            HttpSecurity http, AccountAuthenticationFailureHandler.Exhaustive authenticationFailureHandler)
            throws Exception {
        return http.with(
                        new Oauth2ResourceServerIntegratedAuthenticationHttpConfigurer(authenticationFailureHandler),
                        withDefaults())
                .build();
    }

    @Override
    public void configure(HttpSecurity http) {
        final var applicationContext = http.getSharedObject(ApplicationContext.class);
        final var authenticationManager = applicationContext.getBean(AuthenticationManager.class);

        configure(http, authenticationManager);
    }

    protected void configure(HttpSecurity http, AuthenticationManager authenticationManager) {
        http.addFilterBefore(
                AccountAccessTokenAuthenticationFilter.searchingAuthorizationHeader(
                        authenticationManager, authenticationFailureHandler),
                AuthorizationFilter.class);
    }
}


