package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import org.acme.dto.ShortDto;

@ApplicationScoped
public class ShortService {
    @Inject
    EntityManager em;

    public ShortDto generateShortUrl(@Valid ShortDto originalUrl) {
        return originalUrl;
    }
}
