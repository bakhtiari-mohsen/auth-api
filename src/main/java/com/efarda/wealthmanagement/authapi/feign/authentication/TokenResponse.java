package com.efarda.wealthmanagement.authapi.feign.authentication;

public record TokenResponse(boolean isNewUser, String accessToken) {}