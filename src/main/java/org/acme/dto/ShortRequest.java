package org.acme.dto;

import io.smallrye.common.constraint.NotNull;
import org.acme.annotation.NoForbiddenWords;
import org.acme.annotation.OutOfLengthUrl;

public class ShortDto {
    @NotNull
    @OutOfLengthUrl
    @NoForbiddenWords
    private String originalUrl;

    private String customAlias;

    private String expiresAt;

    public ShortDto() {
    }

    public ShortDto(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getCustomAlias() {
        return customAlias;
    }

    public void setCustomAlias(String customAlias) {
        this.customAlias = customAlias;
    }

    public String getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }

}