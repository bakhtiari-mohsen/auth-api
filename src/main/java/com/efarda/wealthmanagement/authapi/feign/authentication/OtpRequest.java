package com.efarda.wealthmanagement.authapi.feign.authentication;

import com.efarda.wealthmanagement.common.validation.MobileConstraint;
import com.efarda.wealthmanagement.common.valueobject.Mobile;
import com.fasterxml.jackson.annotation.JsonProperty;

public record OtpRequest(
        @JsonProperty("mobile")
        @MobileConstraint(message = "mobile.not_valid")
        Mobile mobile
) {
}