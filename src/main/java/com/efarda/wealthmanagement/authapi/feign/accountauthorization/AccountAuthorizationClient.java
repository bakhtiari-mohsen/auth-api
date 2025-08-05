package com.efarda.wealthmanagement.authapi.feign.accountauthorization;

import com.efarda.wealthmanagement.authapi.config.feign.InternalFeignClientConfig;
import com.efarda.wealthmanagement.common.valueobject.AccountId;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component("accountAuthorizationClient")
@FeignClient(name = "accountAuthorizationClient", url = "${auth.service.base-url}", configuration = InternalFeignClientConfig.class)
public interface AccountAuthorizationClient {
    @GetMapping("/v1/account-authorization/verify")
    ResponseEntity<AccountId> verify(@RequestParam("accessToken") String accessToken);
}
