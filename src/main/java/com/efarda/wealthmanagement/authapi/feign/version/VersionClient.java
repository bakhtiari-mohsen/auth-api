package com.efarda.wealthmanagement.authapi.feign.version;

import com.efarda.wealthmanagement.common.config.LoggedFeignClientConfig;
import com.efarda.wealthmanagement.common.enums.PlatformType;
import com.efarda.wealthmanagement.common.valueobject.VersionId;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Component("versionClient")
@FeignClient(name = "versionClient", url = "${auth.service.base-url}", configuration = LoggedFeignClientConfig.class)
public interface VersionClient {

    @PostMapping
    ResponseEntity<VersionResponse> create(@RequestBody VersionRequest version);

    @GetMapping("/v1/version/{versionId}")
    ResponseEntity<VersionResponse> get(@PathVariable("versionId") VersionId versionId);

    @GetMapping("/v1/version/list/{platformType}")
    ResponseEntity<List<VersionResponse>> list(@PathVariable("platformType") PlatformType platformType);

    @DeleteMapping("/v1/version/{id}")
    void delete(@PathVariable VersionId id);
}
