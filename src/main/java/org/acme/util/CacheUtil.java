package org.acme.util;

import io.quarkus.logging.Log;
import io.quarkus.redis.datasource.RedisDataSource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class CacheUtil {
    @Inject
    RedisDataSource redis;

    @ConfigProperty(name = "app.cache.ttl-seconds", defaultValue = "600")
    int cacheTtlSeconds;

    public String getCacheKey(String key) {
        return key;
    }

    // <T> ประกาศ generic, T ตัวหน้าคือ return type ของ method (ตรงกับ clazz ที่ส่งเข้ามา)
    public <T> T getFromCache(String key, Class<T> clazz) {
        try {
            return redis.value(clazz).get(getCacheKey(key));
        } catch (RuntimeException e) {
            Log.warnf("Redis unavailable, skip cache read for %s: %s", key, e.getMessage());
            return null;
        }
    }

    public <T> void putToCache(String code, T value, Class<T> clazz) {
        try {
            redis.value(clazz).setex(getCacheKey(code), cacheTtlSeconds, value);
        } catch (RuntimeException e) {
            Log.warnf("Redis unavailable, skip cache write for %s: %s", code, e.getMessage());
        }
    }

    public void removeCache(String code) {
        try {
            redis.key().del(getCacheKey(code));
        } catch (RuntimeException e) {
            Log.warnf("Redis unavailable, cache for %s may be stale up to %ds: %s", code, cacheTtlSeconds, e.getMessage());
        }
    }
}
