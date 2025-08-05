package com.efarda.wealthmanagement.authapi.account;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Objects;

public final class AccountScope {
    private static final String SEPARATOR = ":";
    private static final int CATEGORY_INDEX = 0;
    private static final int GROUP_INDEX = 1;

    private final @JsonProperty("value") String value;
    private final @JsonProperty("category") String category;
    private final @JsonProperty("assignedAt") ZonedDateTime assignedAt;

    // Nullness of this property means that the scope never expires
    private final @JsonProperty("expireAfter") @Nullable Duration expireAfter;

    @JsonCreator
    private AccountScope(
            @JsonProperty("value") String value,
            @JsonProperty("category") String category,
            @JsonProperty("assignedAt") ZonedDateTime assignedAt,
            @JsonProperty("expireAfter") @Nullable Duration expireAfter) {
        this.value = value;
        this.category = category;
        this.assignedAt = assignedAt;
        this.expireAfter = expireAfter;
    }

    public static AccountScope ofStringNeverExpired(String scope) {
        final var category = extractCategory(scope);
        return new AccountScope(scope, category, ZonedDateTime.now(), null);
    }

    public static AccountScope ofString(String scope, Duration expireAfter) {
        final var category = extractCategory(scope);
        return new AccountScope(scope, category, ZonedDateTime.now(), expireAfter);
    }

    private static String extractCategory(String scope) {
        return scope.split(SEPARATOR)[CATEGORY_INDEX];
    }

    @JsonIgnore
    private boolean willItExpire() {
        return expireAfter != null;
    }

    @JsonIgnore
    public String getName() {
        return value;
    }

    @JsonIgnore
    public String getGroup() {
        return value.split(SEPARATOR)[GROUP_INDEX];
    }

    @JsonIgnore
    public String getPersianNameCode() {
        return value.replaceAll(":", ".");
    }

    @JsonIgnore
    public String getDescriptionCode() {
        return getPersianNameCode() + ".description";
    }

    @JsonIgnore
    public String getCategory() {
        return category;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final var that = (AccountScope) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        if (!willItExpire()) return "'%s' assignedAt '%s'".formatted(value, assignedAt.toString());

        return "'%s' assignedAt '%s' expireAfter %s".formatted(value, assignedAt.toString(), expireAfter.toString());
    }
}
