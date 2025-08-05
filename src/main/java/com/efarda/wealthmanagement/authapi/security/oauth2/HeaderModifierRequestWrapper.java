package com.efarda.wealthmanagement.authapi.security.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;

import java.util.*;

final class HeaderModifierRequestWrapper extends HttpServletRequestWrapper {
    private final Map<String, String> addedHeaders = new HashMap<>();
    private final List<String> removedHeaders = new ArrayList<>();

    HeaderModifierRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    void addHeader(String name, String value) {
        addedHeaders.put(name, value);
    }

    void removeHeader(String name) {
        removedHeaders.add(name);
    }

    void removeAuthorizationHeader() {
        removeHeader(HttpHeaders.AUTHORIZATION);
    }

    void setAuthorizationHeader(String value) {
        addHeader(HttpHeaders.AUTHORIZATION, value);
    }

    @Override
    public @Nullable String getHeader(String name) {
        if (addedHeaders.containsKey(name)) return addedHeaders.get(name);
        if (removedHeaders.contains(name)) return null;

        return super.getHeader(name);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        final var names = new ArrayList<>(Collections.list(super.getHeaderNames()));
        names.addAll(addedHeaders.keySet());
        names.removeAll(removedHeaders);

        return Collections.enumeration(names);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        if (addedHeaders.containsKey(name)) return Collections.enumeration(List.of(addedHeaders.get(name)));
        if (removedHeaders.contains(name)) return Collections.emptyEnumeration();

        return super.getHeaders(name);
    }
}
