package org.acme.api;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dto.ShortRequest;
import org.acme.service.ShortService;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.net.URI;
import java.util.Map;

@Path("/api")
public class ShortResource {
    @Inject
    ShortService shortService;

    @ConfigProperty(name = "app.base-url")
    private String baseUrl;

    @POST
    @Path("/links")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response generateShortUrl(ShortRequest originalUrl) {
        return Response.status(Response.Status.CREATED).entity(shortService.generateShortUrl(originalUrl)).build();
    }

    @GET
    @Path("/links/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getShortUrl(@PathParam("code") String code) {
        var shortResponse = shortService.findByCode(code);

        if (shortResponse == null) {
            return Response.status(Response.Status.NOT_FOUND).entity(
                    Map.of("message", "Short URL not found")
            ).build();
        }

        if (shortResponse.getDeletedAt() != null) {
            return Response.status(Response.Status.GONE).entity(
                    Map.of("deletedAt", shortResponse.getDeletedAt())
            ).build();
        }

        if (shortService.isExpired(shortResponse.getExpiresAt())) {
            return Response.status(Response.Status.GONE).entity(
                    Map.of("expiresAt", shortResponse.getExpiresAt())
            ).build();
        }


        return Response.status(Response.Status.FOUND).location(URI.create(shortResponse.getOriginalUrl())).build();
    }
}
