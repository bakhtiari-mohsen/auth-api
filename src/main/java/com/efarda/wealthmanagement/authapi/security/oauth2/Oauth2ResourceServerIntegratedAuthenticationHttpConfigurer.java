package com.efarda.wealthmanagement.authapi.security.oauth2;

import com.efarda.wealthmanagement.authapi.security.AccountAccessTokenAuthenticationFilter;
import com.efarda.wealthmanagement.authapi.security.AccountAuthenticationFailureHandler;
import com.efarda.wealthmanagement.authapi.security.AccountAuthenticationHttpConfigurer;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

public class Oauth2ResourceServerIntegratedAuthenticationHttpConfigurer extends AccountAuthenticationHttpConfigurer {
    private static final String FARDA_TOKEN_HEADER_KEY = "Farda-Authorization";

    public Oauth2ResourceServerIntegratedAuthenticationHttpConfigurer(
            AccountAuthenticationFailureHandler.Exhaustive authenticationFailureHandler) {
        super(authenticationFailureHandler);
    }

    @Override
    protected void configure(HttpSecurity http, AuthenticationManager authenticationManager) {
        http.addFilterBefore(
                AccountAccessTokenAuthenticationFilter.searchingProvidedHeader(
                        FARDA_TOKEN_HEADER_KEY, authenticationManager, authenticationFailureHandler),
                AuthorizationFilter.class);
        http.addFilterBefore(
                TokenTypeDetectorFilter.putFardaTokenOnHeader(FARDA_TOKEN_HEADER_KEY),
                BearerTokenAuthenticationFilter.class);
    }
}