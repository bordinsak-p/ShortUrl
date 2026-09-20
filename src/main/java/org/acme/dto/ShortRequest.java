package org.acme.dto;

import io.smallrye.common.constraint.NotNull;
import org.acme.annotation.NoForbiddenWords;
import org.acme.annotation.OutOfLengthUrl;
import org.acme.annotation.ValidExpiryDate;
import org.hibernate.validator.constraints.UniqueElements;

public class ShortRequest {
    @NotNull
    @OutOfLengthUrl
    private String originalUrl;

    @NoForbiddenWords
    private String customAlias;

    @ValidExpiryDate
    private String expiresAt;

    public ShortRequest() {
    }

    public ShortRequest(String originalUrl) {
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