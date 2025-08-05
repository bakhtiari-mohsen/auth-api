package com.efarda.wealthmanagement.authapi.security;

import org.springframework.web.method.support.HandlerMethodArgumentResolver;

import java.util.Set;
import java.util.function.Supplier;


@FunctionalInterface
public interface HandlerMethodArgumentResolverSupplier extends Supplier<Set<HandlerMethodArgumentResolver>> {}
