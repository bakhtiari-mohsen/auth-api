package com.efarda.wealthmanagement.authapi.dto;

import com.efarda.wealthmanagement.common.valueobject.AccountId;
import com.efarda.wealthmanagement.common.valueobject.DeviceId;
import com.efarda.wealthmanagement.common.valueobject.Mobile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.ZonedDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AccountAuthorizationDTO {

    private String accessToken;
    private AccountId accountId;
    private DeviceId deviceId;
    private Mobile mobile;
    private Duration expireAfter;
    private boolean isExpired;
    private ZonedDateTime expiredAt;
}
