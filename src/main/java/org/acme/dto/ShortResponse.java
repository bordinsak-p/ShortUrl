package org.acme.dto;

public class ShortResponse {
    private String code;
    private String shortUrl;
    private String expiresAt;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }
}
