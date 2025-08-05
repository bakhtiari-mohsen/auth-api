package com.efarda.wealthmanagement.authapi.authorization;

import com.efarda.wealthmanagement.authapi.cache.CacheService;
import com.efarda.wealthmanagement.common.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Map;

@Slf4j
@Service
public class ClientCredentialService {

    private final RestTemplate restTemplate;
    private final CacheService tokenCache;

    @Value("${auth.service.token-endpoint}")
    private String tokenUrl;

    @Value("${auth.client.id}")
    private String clientId;

    @Value("${auth.client.secret}")
    private String clientSecret;

    @Value("${auth.client.scopes}")
    private String scopes;

    private static final String TOKEN_KEY = "access_token";

    public ClientCredentialService(CacheService tokenCache) {
        this.restTemplate = new RestTemplate();
        this.tokenCache = tokenCache;
    }

    public String getToken() {
        var cachedToken = tokenCache.get(TOKEN_KEY);
        if (cachedToken != null) {
            return cachedToken;
        }

        return fetchNewToken();
    }

    private static String lastChars(String value) {
        return value.length() <= 6 ? value : value.substring(value.length() - 6);
    }

    private String fetchNewToken() {
        log.info("Fetching new access token for {}, {}", clientId, lastChars(clientSecret));

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        var requestBody = "grant_type=client_credentials"
                + "&client_id=" + clientId
                + "&client_secret=" + clientSecret
                + "&scope=" + scopes;

        var requestEntity = new HttpEntity<>(requestBody, headers);

        var response = restTemplate.exchange(tokenUrl, HttpMethod.POST, requestEntity, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            var body = response.getBody();
            var accessToken = "Bearer " + body.get("access_token");
            int expiresIn = (Integer) body.get("expires_in");

            tokenCache.put(TOKEN_KEY, accessToken, Duration.ofSeconds(expiresIn - 10));
            return accessToken;
        }

        throw BaseException.serverError("credential.token.failed");
    }
}
