package org.acme.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.acme.constant.ErrorCode;
import org.acme.dto.ErrorResponse;

@Provider
public class AliasAlreadyExistsExceptionMapper implements ExceptionMapper<AliasAlreadyExistsException> {
    @Override
    public Response toResponse(AliasAlreadyExistsException e) {
        return Response.status(Response.Status.CONFLICT)
                .type(MediaType.APPLICATION_JSON)
                .entity(new ErrorResponse("customAlias","Custom alias already in use",ErrorCode.ALIAS_ALREADY_EXISTS))
                .build();
    }
}
