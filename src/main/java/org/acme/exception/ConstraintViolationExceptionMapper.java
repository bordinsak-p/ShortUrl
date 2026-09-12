package org.acme.exception;

import io.smallrye.common.constraint.NotNull;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.acme.annotation.NoForbiddenWords;
import org.acme.annotation.OutOfLengthUrl;
import org.acme.constant.ErrorCode;
import org.acme.dto.ErrorResponse;

import java.lang.annotation.Annotation;
import java.util.List;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
    @Override
    public Response toResponse(ConstraintViolationException e) {
        List<ErrorResponse> errors = e.getConstraintViolations()
                .stream()
                .map(this::toErrorResponse)
                .toList();

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(errors)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    private ErrorResponse toErrorResponse(ConstraintViolation<?> violation) {
        String field = extractFieldName(violation);
        Annotation annotation = violation.getConstraintDescriptor().getAnnotation();

        String errorCode = switch (annotation) {
            case NotNull notNull -> ErrorCode.FIELD_REQUIRED;
            case NoForbiddenWords noForbiddenWords -> ErrorCode.FORBIDDEN_WORD_DETECTED;
            case OutOfLengthUrl outOfLengthUrl -> ErrorCode.INVALID_LENGTH;
            case null, default -> ErrorCode.VALIDATION_ERROR;
        };

        return new ErrorResponse(field, violation.getMessage(), errorCode);
    }

    private String extractFieldName(ConstraintViolation<?> violation) {
        String path = violation.getPropertyPath().toString();
        return path.contains(".") ? path.substring(path.lastIndexOf('.') + 1) : path;
    }
}
