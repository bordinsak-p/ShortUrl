package org.acme.annotation;

import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface NoForbiddenWords {
    String message() default "Field contains forbidden words";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
