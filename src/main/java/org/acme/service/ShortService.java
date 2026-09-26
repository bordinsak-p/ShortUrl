package org.acme.service;

import io.quarkus.narayana.jta.QuarkusTransaction;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.acme.dto.ShortRequest;
import org.acme.dto.ShortResponse;
import org.acme.entity.ShortUrls;
import org.acme.util.GenerateShort;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.Instant;

@ApplicationScoped
public class ShortService {
    @Inject
    private EntityManager em;

    @ConfigProperty(name = "app.base-url")
    private String baseUrl;

    @Transactional
    public ShortResponse generateShortUrl(@Valid ShortRequest shortRequest) {
        String expiresAt = shortRequest.getExpiresAt() != null ? shortRequest.getExpiresAt() : null;

        if (shortRequest.getCustomAlias() != null) {
            var entity = new ShortUrls();
            entity.setCode(shortRequest.getCustomAlias());
            entity.setOriginalUrl(shortRequest.getOriginalUrl());
            entity.setCustomAlias(shortRequest.getCustomAlias());
            entity.setExpiresAt(expiresAt);
            entity.setDeletedAt(null);
            em.persist(entity);

            return new ShortResponse(entity.getCode(), GenerateShort.buildShortUrl(baseUrl, entity.getCode()), entity.getExpiresAt());

        } else {
            // Retry กรณี generate code ซ้ำกัน (code เป็น unique)
            // แต่ generate code อาจมีโอก่ศนซ้ำกันได้
            for (int attempt = 1; attempt <= 3; attempt++) {
                String shortCode = GenerateShort.generateCode(7);
                try {
                    var entity = new ShortUrls();
                    QuarkusTransaction.requiringNew().run(() -> {
                        entity.setCode(shortCode);
                        entity.setOriginalUrl(shortRequest.getOriginalUrl());
                        entity.setCustomAlias(shortRequest.getCustomAlias());
                        entity.setExpiresAt(expiresAt);
                        entity.setDeletedAt(null);
                        em.persist(entity);
                        em.flush();
                    });
                    return new ShortResponse(entity.getCode(), GenerateShort.buildShortUrl(baseUrl, entity.getCode()), entity.getExpiresAt());
                } catch (PersistenceException e) {
                    if (attempt == 3) {
                        throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
                    }
                }
            }
        }
        throw new IllegalStateException("unreachable");
    }

    public ShortResponse findByCode(String code) {
        var entity = em.createQuery("SELECT s FROM ShortUrls s WHERE s.code = :code", ShortUrls.class)
                .setParameter("code", code)
                .getSingleResultOrNull();

        if (entity == null) {
            return null;
        }

        return new ShortResponse(entity);
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
    }
}
