package org.acme.validator;

import jakarta.validation.ConstraintValidator;
import org.acme.annotation.ValidExpiryDate;

import java.time.Instant;
import java.time.format.DateTimeParseException;

public class ValidExpiryDateValidator implements ConstraintValidator<ValidExpiryDate, String> {
    @Override
    public boolean isValid(String value, jakarta.validation.ConstraintValidatorContext context) {
        if (value == null) {
            return true; // ปล่อยให้ @NotNull จัดการเรื่อง null แยก
        }
        try {
            /**
             * การเช็ค isAfter(Instant.now()) คือเทียบกันระดับวินาที ไม่ใช่ระดับวัน
             *
             * ตัวอย่าง: ถ้าตอนนี้เวลา 2026-09-20T15:00:00Z
             *
             * expires_at = 2026-09-20T23:00:00Z (วันนี้ ตอนหัวค่ำ) → ผ่าน เพราะยังมาไม่ถึง ยังเป็นอนาคตอยู่
             * expires_at = 2026-09-20T09:00:00Z (วันนี้ ตอนเช้า) → ไม่ผ่าน เพราะเวลานั้นผ่านไปแล้วจริงๆ ตั้งแต่ตอนเช้า
             */
            Instant expiry = Instant.parse(value);
            return expiry.isAfter(Instant.now());
        } catch (DateTimeParseException e) {
            return false; // format ผิด เช่น ไม่มี Z ต่อท้าย หรือไม่ใช่ ISO-8601 เลย
        }
    }
}
