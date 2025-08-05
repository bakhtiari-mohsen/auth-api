package com.efarda.wealthmanagement.authapi.config;

import com.efarda.wealthmanagement.authapi.security.AccountIdHandlerMethodArgumentResolver;
import com.efarda.wealthmanagement.authapi.security.MobileHandlerMethodArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final Set<Converter<?, ?>> converters;

    private final AccountIdHandlerMethodArgumentResolver accountIdResolver;
    private final MobileHandlerMethodArgumentResolver mobileResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(accountIdResolver);
        resolvers.add(mobileResolver);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        converters.forEach(registry::addConverter);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("*").allowedHeaders("*").allowedOrigins("*");
    }
}
