package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.acme.dto.ShortRequest;
import org.acme.dto.ShortResponse;
import org.acme.entity.ShortUrls;
import org.acme.util.GenerateShort;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class ShortService {
    @Inject
    private EntityManager em;

    @ConfigProperty(name = "app.base-url")
    private String baseUrl;

    @Transactional
    public ShortResponse generateShortUrl(@Valid ShortRequest shortRequest) {
        String code = shortRequest.getCustomAlias() != null ? shortRequest.getCustomAlias() : GenerateShort.generateCode(7);
        String expiresAt = shortRequest.getExpiresAt() != null ? shortRequest.getExpiresAt() : null;

        var entity = new ShortUrls();
        entity.setCode(code);
        entity.setOriginalUrl(shortRequest.getOriginalUrl());
        entity.setCustomAlias(shortRequest.getCustomAlias());
        entity.setExpiresAt(expiresAt);
        entity.setDeletedAt(null);
        em.persist(entity);

        return new ShortResponse(entity.getCode(), GenerateShort.buildShortUrl(baseUrl, entity.getCode()), entity.getExpiresAt());
    }

    public ShortResponse getShortUrl(String code) {
        var entity = em.createQuery("SELECT s FROM ShortUrls s WHERE s.code = :code AND s.deletedAt IS NULL", ShortUrls.class)
                .setParameter("code", code)
                .getSingleResultOrNull();

        if (entity == null) {
            return null;
        }

        return new ShortResponse(entity.getCode(), GenerateShort.buildShortUrl(baseUrl, entity.getCode()), entity.getExpiresAt());
    }
}
