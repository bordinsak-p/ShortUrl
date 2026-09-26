package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.acme.dto.ShortRequest;
import org.acme.dto.ShortResponse;
import org.acme.service.ShortService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
    }


}
