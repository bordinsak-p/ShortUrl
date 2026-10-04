package org.acme.validator;

import jakarta.inject.Inject;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.acme.annotation.NoForbiddenWords;
import org.acme.service.ForbiddenWordsService;

public class NoForbiddenWordsValidator implements ConstraintValidator<NoForbiddenWords, String> {
    @Inject
    ForbiddenWordsService forbiddenWordsService;

    // ถ้า value ไม่ตรงกันทั้งหมด return true
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // ปล่อยให้ @NotNull จัดการเรื่อง null แยก
        }
        return forbiddenWordsService
                .getWordsList()
                .stream()
                .anyMatch(
                        forbiddenWord -> value.toLowerCase().contains(forbiddenWord)
                );
    }
}
