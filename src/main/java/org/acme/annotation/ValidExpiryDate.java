package org.acme.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.acme.validator.ValidExpiryDateValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidExpiryDateValidator.class)
public @interface ValidExpiryDate {
    String message() default "expiresAt must be a valid ISO-8601 UTC datetime in the future";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
