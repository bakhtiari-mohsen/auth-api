package com.efarda.wealthmanagement.authapi.feign.authentication;

import com.efarda.wealthmanagement.common.config.LoggedFeignClientConfig;
import jakarta.validation.Valid;
import com.efarda.wealthmanagement.common.valueobject.AccountId;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "authenticationClient", url = "${auth.service.base-url}", configuration = LoggedFeignClientConfig.class)
public interface AuthenticationClient {
    @PostMapping("/v1/authentication/authentication/otp")
    ResponseEntity<Void> otp(@Valid @RequestBody OtpRequest request);

    @PostMapping("/v1/authentication/authentication/token")
    ResponseEntity<TokenResponse> token(@Valid @RequestBody TokenRequest request);

    @DeleteMapping("/v1/authentication/sign-out/{accountId}")
    ResponseEntity<Void> logout(@PathVariable AccountId accountId);
}
