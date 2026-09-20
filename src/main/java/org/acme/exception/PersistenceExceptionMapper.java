package org.acme.exception;

import jakarta.persistence.PersistenceException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

import jakarta.ws.rs.ext.Provider;
import org.acme.constant.ErrorCode;
import org.acme.dto.ErrorResponse;

@Provider
public class PersistenceExceptionMapper implements ExceptionMapper<PersistenceException> {
    @Override
    public Response toResponse(PersistenceException e) {
        return Response.status(Response.Status.CONFLICT)
                .entity(new ErrorResponse("customAlias", "Custom alias already in use", ErrorCode.ALIAS_ALREADY_EXISTS))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
