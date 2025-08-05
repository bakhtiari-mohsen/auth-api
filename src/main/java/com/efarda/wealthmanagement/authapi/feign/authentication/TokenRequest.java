package com.efarda.wealthmanagement.authapi.feign.authentication;

import com.efarda.wealthmanagement.authapi.importer.DeviceImporter;
import com.efarda.wealthmanagement.common.enums.PlatformType;
import com.efarda.wealthmanagement.common.validation.DeviceIdConstraint;
import com.efarda.wealthmanagement.common.validation.MobileConstraint;
import com.efarda.wealthmanagement.common.valueobject.AccountId;
import com.efarda.wealthmanagement.common.valueobject.DeviceId;
import com.efarda.wealthmanagement.common.valueobject.Mobile;
import com.efarda.wealthmanagement.common.valueobject.VersionId;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TokenRequest implements DeviceImporter {

        @JsonProperty("mobile")
        @MobileConstraint(message = "mobile.not_valid")
        private Mobile mobile;

        @JsonProperty("otp")
        @NotEmpty(message = "otp.not_empty")
        private String otp;

        @DeviceIdConstraint(message = "deviceId.not_valid")
        private DeviceId deviceId;

        @Setter
        private AccountId accountId;

        private String model;
        private String brand;
        private PlatformType platform;
        private String osVersion;
        private String userAgent;
        private VersionId versionId;

        @Override
        public DeviceId deviceId() {
                return this.deviceId;
        }

        @Override
        public AccountId accountId() {
                return this.accountId;
        }

        @Override
        public String model() {
                return this.model;
        }

        @Override
        public String brand() {
                return this.brand;
        }

        @Override
        public PlatformType platform() {
                return this.platform;
        }

        @Override
        public String osVersion() {
                return this.osVersion;
        }

        @Override
        public String userAgent() {
                return this.userAgent;
        }

        @Override
        public VersionId versionId() {
                return this.versionId;
        }
}
