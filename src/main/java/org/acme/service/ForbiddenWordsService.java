package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.acme.dto.ForbiddenWords;
import org.acme.util.CacheUtil;

import java.util.List;

@ApplicationScoped
public class ForbiddenWordsService {
    @Inject
    EntityManager em;

    @Inject
    CacheUtil cache;

    private static final String KEY = "forbiddenWords:";

    @SuppressWarnings("unchecked")
    public List<String> getWordsList() {
        // Try cache first
        ForbiddenWords forbiddenWordsCached = cache.getFromCache(KEY, ForbiddenWords.class);
        if(forbiddenWordsCached != null) {
            return forbiddenWordsCached.words();
        }

        // Cache miss - fetch from database
        List<String> words = em.createNativeQuery("SELECT LOWER(word) FROM forbidden_words").getResultList();

        // Store in cache with TTL
        cache.putToCache(KEY, new ForbiddenWords(words), ForbiddenWords.class);

        return  words;
    }


}
