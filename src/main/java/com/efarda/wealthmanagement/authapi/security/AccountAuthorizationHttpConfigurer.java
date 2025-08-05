package com.efarda.wealthmanagement.authapi.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import static org.springframework.security.config.Customizer.withDefaults;

public class AccountAuthorizationHttpConfigurer
        extends AbstractHttpConfigurer<AccountAuthorizationHttpConfigurer, HttpSecurity> {

    public static HttpSecurity accountsShouldHaveMandatoryInfo(HttpSecurity http) throws Exception {
        return http.with(new AccountAuthorizationHttpConfigurer(), withDefaults());
    }

    @Override
    public void configure(HttpSecurity http) {
        final var applicationContext = http.getSharedObject(ApplicationContext.class);
        final var objectMapper = applicationContext.getBean(ObjectMapper.class);

        configure(http, objectMapper);
    }

    private void configure(HttpSecurity http, ObjectMapper objectMapper) {
        http.addFilterAfter(new AccountAuthorizationFilter(objectMapper), AccountAccessTokenAuthenticationFilter.class);
    }
}
