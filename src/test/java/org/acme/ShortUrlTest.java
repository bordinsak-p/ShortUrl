package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.acme.dto.ShortRequest;
import org.acme.dto.ShortResponse;
import org.acme.service.ShortService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.sqlite.SQLiteException;

import java.time.Duration;
import java.time.Instant;

@QuarkusTest
class ShortUrlTest {

    @Inject
    ShortService shortService;

    @Test
    void generateShortUrl() {
        var req = new ShortRequest();
        req.setOriginalUrl("https://www.youtube.com/watch?v=KmxEbeb-2DQ&list=RDKmxEbeb-2DQ&start_radio=1");
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
        req.setOriginalUrl("https://www.youtube.com/watch?v=KmxEbeb-2DQ&list=RDKmxEbeb-2DQ&start_radio=1");
        req.setExpiresAt(exp);
        req.setCustomAlias(null);

        ShortResponse shortResponse = shortService.generateShortUrl(req);

        Assertions.assertNotNull(shortResponse.getExpiresAt());
    }

    @Test
    void generateShortUrlByCustomAlias() {
        var req = new ShortRequest();
        req.setOriginalUrl("https://www.youtube.com/watch?v=KmxEbeb-2DQ&list=RDKmxEbeb-2DQ&start_radio=1");
        req.setExpiresAt(null);
        req.setCustomAlias("test-alias");

        ShortResponse shortResponse = shortService.generateShortUrl(req);

        Assertions.assertNull(shortResponse.getExpiresAt());
        Assertions.assertEquals(req.getCustomAlias(), shortResponse.getCode());
    }

    @Test
    void  generateShortUrlSameCustomAlias() {
        var req = new ShortRequest();
        req.setOriginalUrl("https://www.youtube.com/watch?v=KmxEbeb-2DQ&list=RDKmxEbeb-2DQ&start_radio=1");
        req.setExpiresAt(null);
        req.setCustomAlias("test-alias");

        Exception ex = Assertions.assertThrows(Exception.class,() -> shortService.generateShortUrl(req));   // ครั้งที่สอง ต้องพัง

        Throwable root = rootCause(ex);
        Assertions.assertInstanceOf(SQLiteException.class, root);
        Assertions.assertTrue(root.getMessage().contains("SQLITE_CONSTRAINT_UNIQUE"));
    }
    private static Throwable rootCause(Throwable t) {
        while (t.getCause() != null && t.getCause() != t) {
            t = t.getCause();
        }
        return t;
    }

}
