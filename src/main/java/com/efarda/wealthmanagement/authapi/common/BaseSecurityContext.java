package com.efarda.wealthmanagement.authapi.common;

import com.efarda.wealthmanagement.authapi.config.CurrentAccountIdHolder;
import com.efarda.wealthmanagement.common.exception.BaseException;
import com.efarda.wealthmanagement.common.valueobject.AccountId;
import lombok.extern.slf4j.Slf4j;

import static java.util.Objects.isNull;

@Slf4j
public class BaseSecurityContext {
    public static AccountId currentUser() {
        final var currentAccountId = CurrentAccountIdHolder.getCurrentAccountId();

        if (isNull(currentAccountId))
            throw BaseException.forbidden("wm.currentUser.forbidden");

        return currentAccountId;
    }
}