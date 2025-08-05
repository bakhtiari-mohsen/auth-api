package com.efarda.wealthmanagement.authapi.feign.version;

import com.efarda.wealthmanagement.common.enums.PlatformType;
import com.efarda.wealthmanagement.common.valueobject.VersionId;
import com.fasterxml.jackson.annotation.JsonProperty;

public record VersionResponse(
        @JsonProperty("id") VersionId id,
        @JsonProperty("platform") PlatformType platform,
        @JsonProperty("platformVersion") String platformVersion,
        @JsonProperty("buildNo") long buildNo,
        @JsonProperty("description") String description,
        @JsonProperty("published") boolean published,
        @JsonProperty("releaseNote") String releaseNote,
        @JsonProperty("active") boolean active
) {
}
