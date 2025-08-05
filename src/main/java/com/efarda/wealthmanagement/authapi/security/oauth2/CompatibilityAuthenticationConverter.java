package com.efarda.wealthmanagement.authapi.security.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;

@RequiredArgsConstructor
final class CompatibilityAuthenticationConverter implements AuthenticationConverter {
    private final AuthenticationConverter delegate;

    @Nullable
    @Override
    public Authentication convert(HttpServletRequest request) {
        return delegate.convert(new RequestWrapper(request));
    }

    private static final class RequestWrapper extends HttpServletRequestWrapper {

        private RequestWrapper(HttpServletRequest request) {
            super(request);
        }

        @Override
        public @Nullable String getQueryString() {
            return null;
        }
    }
}
