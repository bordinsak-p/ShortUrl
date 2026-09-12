package org.acme.dto;

import io.smallrye.common.constraint.NotNull;

public class ShortDto {
    @NotNull
    private String code;

    @NotNull
    private String originalUrl;

    public ShortDto() {
    }

    public ShortDto(String code, String originalUrl) {
        this.code = code;
        this.originalUrl = originalUrl;
    }

}
