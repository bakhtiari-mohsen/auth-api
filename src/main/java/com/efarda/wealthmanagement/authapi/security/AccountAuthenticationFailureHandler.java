package com.efarda.wealthmanagement.authapi.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.IOException;

public abstract class AccountAuthenticationFailureHandler {

    public static Exhaustive unauthenticatedUnauthorized() {
        final var unauthorized = new Unauthorized();
        return new Exhaustive(unauthorized, unauthorized);
    }

    public static Exhaustive unauthenticatedForbidden() {
        final var forbidden = new Forbidden();
        return new Exhaustive(forbidden, forbidden);
    }

    protected abstract void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException;

    protected abstract boolean couldHandleAuthenticationFailure(HttpServletRequest request);

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    private static final class Unauthorized extends AccountAuthenticationFailureHandler {

        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }

        @Override
        public boolean couldHandleAuthenticationFailure(HttpServletRequest request) {
            // Could handle all request
            return true;
        }
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    private static final class Forbidden extends AccountAuthenticationFailureHandler {

        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
        }

        @Override
        public boolean couldHandleAuthenticationFailure(HttpServletRequest request) {
            // Could handle all request
            return true;
        }
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Exhaustive extends AccountAuthenticationFailureHandler {
        private final AccountAuthenticationFailureHandler delegate;
        private final AccountAuthenticationFailureHandler defaultHandler;

        @Override
        protected void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response)
                throws IOException, ServletException {
            if (delegate.couldHandleAuthenticationFailure(request)) {
                delegate.onAuthenticationFailure(request, response);
                return;
            }

            defaultHandler.onAuthenticationFailure(request, response);
        }

        @Override
        protected boolean couldHandleAuthenticationFailure(HttpServletRequest request) {
            // This is an exhaustive failure handler, so handle all requests
            return true;
        }

        Exhaustive withDefaultHandler(AccountAuthenticationFailureHandler defaultHandler) {
            return new Exhaustive(delegate, defaultHandler);
        }
    }
}
