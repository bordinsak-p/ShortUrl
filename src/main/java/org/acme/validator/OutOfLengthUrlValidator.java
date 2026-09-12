package org.acme.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.acme.annotation.OutOfLengthUrl;

public class OutOfLengthUrlValidator implements ConstraintValidator<OutOfLengthUrl, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if(value == null) return true; // ปล่อยให้ @NotNull จัดการเรื่อง null แยก
        return value.length() <= 2048; // ตรวจสอบความยาวของ URL ไม่เกิน 2048 ตัวอักษร
    }
}
