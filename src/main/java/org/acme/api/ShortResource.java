package org.acme.api;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dto.ShortDto;
import org.acme.service.ShortService;

@Path("/api")
public class ShortResource {
    @Inject
    ShortService shortService;

    @POST
    @Path("/links")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response generateShortUrl(ShortDto originalUrl) {
        return Response.ok(shortService.generateShortUrl(originalUrl)).build();
    }
}
