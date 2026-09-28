package org.acme.exception;

import io.quarkus.logging.Log;
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
        Log.error("Unexpected database error", e);   // สำคัญที่สุด: ต้องเห็นใน log
        return Response.serverError()
                .type(MediaType.APPLICATION_JSON)
                .entity(new ErrorResponse(null,"Internal server error",ErrorCode.INTERNAL_SERVER_ERROR))
                .build();
    }
}
