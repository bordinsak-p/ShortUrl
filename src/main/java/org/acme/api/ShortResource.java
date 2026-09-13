package org.acme.api;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dto.ShortRequest;
import org.acme.service.ShortService;

@Path("/api")
public class ShortResource {
    @Inject
    ShortService shortService;

    @POST
    @Path("/links")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response generateShortUrl(ShortRequest originalUrl) {
        return Response.ok(shortService.generateShortUrl(originalUrl)).build();
    }

    @GET
    @Path("/links/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getShortUrl(@PathParam("code") String code) {
        var shortResponse = shortService.getShortUrl(code);
        if (shortResponse == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(shortResponse).build();
    }
}
