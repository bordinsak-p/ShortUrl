package org.acme.service;

import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.value.ValueCommands;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.acme.dto.ForbiddenWords;

import java.util.List;

@ApplicationScoped
public class ForbiddenWordsService {
    @Inject
    EntityManager em;

    @Inject
    RedisDataSource redis;

    private static final String KEY = "forbiddenWords:";

    @SuppressWarnings("unchecked")
    public List<String> getWordsList() {
        ValueCommands<String, ForbiddenWords> initCache = redis.value(ForbiddenWords.class);

        // Try cache first
        ForbiddenWords forbiddenWordsCached = initCache.get(KEY);
        if(forbiddenWordsCached != null) {
            return forbiddenWordsCached.words();
        }

        // Cache miss - fetch from database
        List<String> words = em.createNativeQuery("SELECT LOWER(word) FROM forbidden_words").getResultList();

        // Store in cache with TTL
        initCache.setex(KEY, 600, new ForbiddenWords(words));

        return  words;
    }


}
