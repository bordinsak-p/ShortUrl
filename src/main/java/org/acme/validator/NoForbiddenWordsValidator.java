package org.acme.validator;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.acme.annotation.NoForbiddenWords;

import java.util.List;

public class NoForbiddenWordsValidator implements ConstraintValidator<NoForbiddenWords, String> {
    @Inject
    EntityManager em;

    @SuppressWarnings("unchecked")
    private List<String> getWordsList() {
        return (List<String>) em.createNativeQuery("SELECT word FROM forbidden_words").getResultList();
    }

    // ถ้า value ไม่ตรงกันทั้งหมด return true
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // ปล่อยให้ @NotNull จัดการเรื่อง null แยก
        }
        return getWordsList().stream().noneMatch(forbiddenWord -> value.toLowerCase().contains(forbiddenWord));
    }
}
