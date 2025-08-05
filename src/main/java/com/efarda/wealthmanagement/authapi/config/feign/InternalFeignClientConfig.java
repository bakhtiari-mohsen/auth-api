package com.efarda.wealthmanagement.authapi.config.feign;

import com.efarda.wealthmanagement.authapi.authorization.ClientCredentialService;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.RequestInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
@RequiredArgsConstructor
public class InternalFeignClientConfig {

    private final ObjectMapper objectMapper;
    private final ClientCredentialService clientCredentialService;

    /*@Bean("customObjectMapper")
    public ObjectMapper customObjectMapper() {
        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        return objectMapper;
    }*/

    @Bean
    public Encoder feignEncoder() {
        var jacksonConverter = new MappingJackson2HttpMessageConverter(objectMapper);

        var httpMessageConverters = new HttpMessageConverters(jacksonConverter);
        ObjectFactory<HttpMessageConverters> objectFactory = () -> httpMessageConverters;

        return new SpringEncoder(objectFactory);
    }

    @Bean
    public Decoder feignDecoder() {
        var jacksonConverter = new MappingJackson2HttpMessageConverter(objectMapper);

        var httpMessageConverters = new HttpMessageConverters(jacksonConverter);
        ObjectFactory<HttpMessageConverters> objectFactory = () -> httpMessageConverters;

        return new ResponseEntityDecoder(new SpringDecoder(objectFactory));
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            final var requestUrl = requestTemplate.url();
            // TODO : CHECK_FLAG
            //  added for noavaran token issue
            var authorizationHeaders = requestTemplate.headers().get("Authorization");
            if (authorizationHeaders != null && !authorizationHeaders.isEmpty()) {
                return;
            }

            if (!requestUrl.contains("/oauth2/token")) {
                final var clientCredentialToken = clientCredentialService.getToken();
                requestTemplate.header("Authorization", clientCredentialToken);
            }
        };
    }
}
