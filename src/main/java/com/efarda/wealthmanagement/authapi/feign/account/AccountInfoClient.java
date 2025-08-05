package com.efarda.wealthmanagement.authapi.feign.account;

import com.efarda.wealthmanagement.authapi.dto.AccountInfo;
import com.efarda.wealthmanagement.common.config.LoggedFeignClientConfig;
import com.efarda.wealthmanagement.common.valueobject.AccountId;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component("accountInfoClient")
@FeignClient(name = "accountInfoClient", url = "${auth.service.base-url}", configuration = LoggedFeignClientConfig.class)
public interface AccountInfoClient {
    @GetMapping("/v1/accounts/info/{accountId}")
    AccountInfo info(@PathVariable AccountId accountId);
}
