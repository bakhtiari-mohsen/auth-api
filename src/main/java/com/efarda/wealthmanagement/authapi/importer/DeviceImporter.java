package com.efarda.wealthmanagement.authapi.importer;

import com.efarda.wealthmanagement.common.enums.PlatformType;
import com.efarda.wealthmanagement.common.valueobject.AccountId;
import com.efarda.wealthmanagement.common.valueobject.DeviceId;
import com.efarda.wealthmanagement.common.valueobject.VersionId;

public interface DeviceImporter {

    DeviceId deviceId();

    AccountId accountId();

    String model();

    String brand();

    PlatformType platform();

    String osVersion();

    String userAgent();

    VersionId versionId();
}
