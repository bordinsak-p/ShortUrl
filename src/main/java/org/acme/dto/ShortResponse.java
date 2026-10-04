package org.acme.dto;

public class ShortResponse {
    private String code;
    private String shortUrl;
    private String expiresAt;
    private String deletedAt;
    private String originalUrl;

    public ShortResponse() {
    }

    public ShortResponse(String code) {
        this.code = code;
    }

    public ShortResponse(String code, String shortUrl) {
        this.code = code;
        this.shortUrl = shortUrl;
    }

    public ShortResponse(String code, String shortUrl, String expiresAt) {
        this.code = code;
        this.shortUrl = shortUrl;
        this.expiresAt = expiresAt;
    }

    public ShortResponse(String code, String shortUrl, String expiresAt, String deletedAt) {
        this.code = code;
        this.expiresAt = expiresAt;
        this.shortUrl = shortUrl;
        this.deletedAt = deletedAt;
    }

    public ShortResponse(String code, String shortUrl, String expiresAt, String deletedAt, String originalUrl) {
        this.code = code;
        this.expiresAt = expiresAt;
        this.shortUrl = shortUrl;
        this.deletedAt = deletedAt;
        this.originalUrl = originalUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }
}
