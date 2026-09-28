package org.acme.api;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.constant.Message;
import org.acme.dto.DeleteShortResponse;
import org.acme.dto.ShortRequest;
import org.acme.service.ShortService;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.net.URI;
import java.util.Map;

@Path("/api")
public class ShortResource {
    @Inject
    ShortService shortService;

    @Inject
    Template preview; // preview คือชื่อ file template.html ต้องตั้งชื่อให้ตรงกัน

    @Inject
    Template linkError;

    private static final String TITLE = "title";
    private static final String STATUS = "status";
    private static final String DETAIL = "detail";


    @ConfigProperty(name = "app.base-url")
    private String baseUrl;

    @POST
    @Path("/links")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response generateShortUrl(@Valid ShortRequest originalUrl) {
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

    @GET
    @Path("/links/{code: [A-Za-z0-9_-]+}/preview")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance preview(@PathParam("code") String code) {
        var byCode = shortService.findByCode(code);

        if (byCode == null) {
            return linkError.data(TITLE, Message.LINK_NOT_FOUND)
                    .data(STATUS, Response.Status.NOT_FOUND.getStatusCode())
                    .data(DETAIL, Message.LINK_NOT_FOUND_DETAIL);
        }

        if (byCode.getDeletedAt() != null) {
            return linkError.data(TITLE, Message.LINK_REMOVED)
                    .data(STATUS, Response.Status.GONE.getStatusCode())
                    .data(DETAIL, Message.LINK_REMOVED_DETAIL);
        }

        if (shortService.isExpired(byCode.getExpiresAt())) {
            return linkError.data(TITLE, Message.LINK_EXPIRED)
                    .data(STATUS, Response.Status.GONE.getStatusCode())
                    .data(DETAIL, Message.LINK_EXPIRED_DETAIL + byCode.getExpiresAt());
        }

        return preview.data("originalUrl", byCode.getOriginalUrl());
    }

    @DELETE
    @Path("links/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteShortUrl(@PathParam("code") String code) {
        var byCode = shortService.findByCode(code);

        if (byCode == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (byCode.getDeletedAt() != null) {
            return Response.status(Response.Status.GONE).entity(
                    new DeleteShortResponse("deleted")
            ).build();
        }

        shortService.deleteShort(code);

        return Response.status(Response.Status.NO_CONTENT).build();

    }
}
