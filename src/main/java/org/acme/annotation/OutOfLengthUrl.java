package org.acme.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.acme.validator.OutOfLengthUrlValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = OutOfLengthUrlValidator.class)
public @interface OutOfLengthUrl {
    String message() default "URL length must not exceed 2048 characters";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
