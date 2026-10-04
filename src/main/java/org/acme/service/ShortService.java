package org.acme.service;

import io.quarkus.logging.Log;
import io.quarkus.narayana.jta.QuarkusTransaction;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.acme.dto.ShortRequest;
import org.acme.dto.ShortResponse;
import org.acme.entity.ShortUrls;
import org.acme.exception.AliasAlreadyExistsException;
import org.acme.util.CacheUtil;
import org.acme.util.GenerateShort;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.Instant;

@ApplicationScoped
public class ShortService {
    @Inject
    private EntityManager em;

    @Inject
    CacheUtil cache;

    @ConfigProperty(name = "app.base-url")
    private String baseUrl;

    private static final int MAX_ATTEMPTS = 3;
    private static final int CODE_LENGTH = 7;

    private static final String SHORT_KEY = "short";

    private String cacheKey(String code) {
        return SHORT_KEY + ":" + code;
    }

    @Transactional
    public ShortResponse generateShortUrl(ShortRequest shortRequest) {
        String expiresAt = shortRequest.getExpiresAt() != null ? shortRequest.getExpiresAt() : null;

        if (shortRequest.getCustomAlias() != null) {
            var entity = new ShortUrls();
            entity.setCode(shortRequest.getCustomAlias());
            entity.setOriginalUrl(shortRequest.getOriginalUrl());
            entity.setCustomAlias(shortRequest.getCustomAlias());
            entity.setExpiresAt(expiresAt);
            entity.setDeletedAt(null);

            try {
                em.persist(entity);
                em.flush();
            } catch (PersistenceException e) {
                throw new AliasAlreadyExistsException("Custom alias already in use");
            }

            return new ShortResponse(entity.getCode(), GenerateShort.buildShortUrl(baseUrl, entity.getCode()), entity.getExpiresAt());

        } else {
            // Retry กรณี generate code ซ้ำกัน (code เป็น unique)
            // แต่ generate code อาจมีโอก่ศนซ้ำกันได้
            for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
                String shortCode = GenerateShort.generateCode(CODE_LENGTH);

                var entity = new ShortUrls();
                entity.setCode(shortCode);
                entity.setOriginalUrl(shortRequest.getOriginalUrl());
                entity.setCustomAlias(shortRequest.getCustomAlias());
                entity.setExpiresAt(expiresAt);
                entity.setDeletedAt(null);

                try {
                    QuarkusTransaction.requiringNew().run(() -> {
                        em.persist(entity);
                        em.flush();
                    });
                    return new ShortResponse(entity.getCode(), GenerateShort.buildShortUrl(baseUrl, entity.getCode()), entity.getExpiresAt());
                } catch (PersistenceException e) {
                    if (attempt == MAX_ATTEMPTS) {
                        throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
                    }
                    Log.warnf("Short code collision: %s (attempt %d/%d)", entity.getCode(), attempt, MAX_ATTEMPTS);
                }
            }
        }
        throw new WebApplicationException("Cannot generate unique short code", Response.Status.INTERNAL_SERVER_ERROR);
    }

    public ShortResponse findByCode(String code) {
        var shortCached = cache.getFromCache(cacheKey(code), ShortResponse.class);

        if (shortCached != null) {
            return shortResponse(shortCached);
        }

        ShortUrls entity = em.createQuery("SELECT s FROM ShortUrls s WHERE s.code = :code", ShortUrls.class)
                .setParameter("code", code)
                .getSingleResultOrNull();

        if (entity == null) {
            return null;
        }

        var response = new ShortResponse(
                entity.getCode(),
                GenerateShort.buildShortUrl(baseUrl, entity.getCode()),
                entity.getExpiresAt(),
                entity.getDeletedAt(),
                entity.getOriginalUrl());

        cache.putToCache(cacheKey(code), response, ShortResponse.class);

        return response;
    }

    private ShortResponse shortResponse(ShortResponse val) {
        var shortUrl = GenerateShort.buildShortUrl(baseUrl, val.getCode());
        return new ShortResponse(val.getCode(), shortUrl, val.getExpiresAt(), val.getDeletedAt(), val.getOriginalUrl());
    }

    public boolean isExpired(String expiresAt) {
        if (expiresAt == null) {
            return false;
        }

        Instant expiresDate = Instant.parse(expiresAt);

        return Instant.now().isAfter(expiresDate);
    }

    @Transactional
    public void deleteShort(String code) {
        em.createQuery("""
                UPDATE ShortUrls s
                    SET s.deletedAt = :now
                WHERE s.code = :code
                    AND s.deletedAt IS NULL
                """)
                .setParameter("now", Instant.now().toString())
                .setParameter("code", code)
                .executeUpdate();

        // ลบ cache เก่าทิ้ง
        cache.removeCache(cacheKey(code));
    }
}
