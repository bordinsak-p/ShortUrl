package org.acme.validator;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintValidator;
import org.acme.annotation.NoForbiddenWords;

import java.util.List;

public class NoForbiddenWordsValidator implements ConstraintValidator<NoForbiddenWords, String> {
    @Inject
    EntityManager em;

    private List<String> getWordsList() {


        return List.of("badword1", "badword2", "badword3");
    }

    @Override
    public boolean isValid(String value, jakarta.validation.ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Consider null as valid, use @NotNull for null checks
        }
//        String[] forbiddenWords = {"badword1", "badword2", "badword3"};
//        for (String forbiddenWord : forbiddenWords) {
//            if (value.toLowerCase().contains(forbiddenWord)) {
//                return false;
//            }
//        }
        return true;
    }
}
