package com.efarda.wealthmanagement.authapi.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static java.util.Objects.isNull;

final class AccountAuthorizationFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper;

    AccountAuthorizationFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        final var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (isNull(authentication)) return true;
        return !(authentication instanceof AuthorizedAccountAuthenticationToken);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final var authenticated = (AuthorizedAccountAuthenticationToken)
                SecurityContextHolder.getContext().getAuthentication();
        final var account = authenticated.getAuthorizedAccount().account();
        if (!account.isPhoneConfirmed()) {
            handleUnprocessableEntity(response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void handleUnprocessableEntity(HttpServletResponse response) throws IOException {
        final var unprocessableEntityResponse =
                new UnprocessableEntityResponse(UnprocessableEntityType.USER_MANDATORY_INFO);

        response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        response.getWriter().write(objectMapper.writeValueAsString(unprocessableEntityResponse));
    }

    private record UnprocessableEntityResponse(@JsonProperty("type") UnprocessableEntityType type) {}
}
