package org.acme.util;

import java.security.SecureRandom;

public class GenerateShort {
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateCode(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }

    public static String buildShortUrl(String baseUrl, String code) {
        if (!baseUrl.endsWith("/")) {
            baseUrl += "/";
        }
        return baseUrl + code;
    }
}
