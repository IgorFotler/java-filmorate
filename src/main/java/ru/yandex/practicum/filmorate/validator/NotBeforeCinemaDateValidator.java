package ru.yandex.practicum.filmorate.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class NotBeforeCinemaDateValidator implements ConstraintValidator<NotBeforeCinemaDate, LocalDate> {

    private static final LocalDate firstFilmDate = LocalDate.of(1895, 12, 28);

    @Override
    public boolean isValid(LocalDate release, ConstraintValidatorContext context) {
        return release != null && release.isAfter(firstFilmDate);
    }
}