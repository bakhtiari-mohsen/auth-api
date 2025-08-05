package com.efarda.wealthmanagement.authapi.feign.version;

import com.efarda.wealthmanagement.common.enums.PlatformType;
import com.fasterxml.jackson.annotation.JsonProperty;

public record VersionRequest(
        @JsonProperty("platform") PlatformType platform,
        @JsonProperty("platformVersion") String platformVersion,
        @JsonProperty("buildNo") long buildNo,
        @JsonProperty("description") String description,
        @JsonProperty("published") boolean published,
        @JsonProperty("releaseNote") String releaseNote,
        @JsonProperty("active") boolean active
) {
}
