package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.acme.dto.ShortRequest;
import org.acme.dto.ShortResponse;
import org.acme.exception.AliasAlreadyExistsException;
import org.acme.service.ShortService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

@QuarkusTest
class ShortUrlServiceTest {

    @Inject
    ShortService shortService;

    @Test
    void generateShortUrl() {
        var req = new ShortRequest();
        req.setOriginalUrl("https://www.wikipedia.org");
        req.setExpiresAt(null);
        req.setCustomAlias(null);

        ShortResponse shortResponse = shortService.generateShortUrl(req);

        Assertions.assertNotNull(shortResponse.getCode());
        Assertions.assertNotNull(shortResponse.getShortUrl());
        Assertions.assertNull(shortResponse.getExpiresAt());
    }

    @Test
    void generateShortUrlByExpire()  {
        String exp = Instant.now().plus(Duration.ofHours(1)).toString();

        var req = new ShortRequest();
        req.setOriginalUrl("https://www.wikipedia.org");
        req.setExpiresAt(exp);
        req.setCustomAlias(null);

        ShortResponse shortResponse = shortService.generateShortUrl(req);

        Assertions.assertNotNull(shortResponse.getExpiresAt());
    }

    @Test
    void generateShortUrlByCustomAlias() {
        var req = new ShortRequest();
        req.setOriginalUrl("https://www.wikipedia.org");
        req.setExpiresAt(null);
        req.setCustomAlias("test-alias");

        ShortResponse shortResponse = shortService.generateShortUrl(req);

        Assertions.assertNull(shortResponse.getExpiresAt());
        Assertions.assertEquals(req.getCustomAlias(), shortResponse.getCode());
    }

    @Test
    void generateShortUrlSameCustomAlias() {
        var req = new ShortRequest();
        req.setOriginalUrl("https://www.wikipedia.org");
        req.setExpiresAt(null);
        req.setCustomAlias("test-alias");

        Exception ex = Assertions.assertThrows(AliasAlreadyExistsException.class,() -> shortService.generateShortUrl(req));   // ครั้งที่สอง ต้องพัง

        Throwable root = rootCause(ex);
        Assertions.assertInstanceOf(AliasAlreadyExistsException.class, root);
        Assertions.assertTrue(root.getMessage().contains("Custom alias already in use"));
    }
    private static Throwable rootCause(Throwable t) {
        while (t.getCause() != null && t.getCause() != t) {
            t = t.getCause();
        }
        return t;
    }

    @Test
    void deleteShortByCode() {
        var req = new ShortRequest();
        req.setOriginalUrl("https://www.wikipedia.org");
        req.setExpiresAt(null);
        req.setCustomAlias(null);

        ShortResponse shortResponse = shortService.generateShortUrl(req);

        shortService.deleteShort(shortResponse.getCode());

        ShortResponse byCode = shortService.findByCode(shortResponse.getCode());

        Assertions.assertNotNull(byCode.getDeletedAt());
    }
}
