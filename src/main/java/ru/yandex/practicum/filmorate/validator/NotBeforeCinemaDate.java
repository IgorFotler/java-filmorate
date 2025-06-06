package ru.yandex.practicum.filmorate.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NotBeforeCinemaDateValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface NotBeforeCinemaDate {
    String message() default "Дата релиза не может быть раньше 28 декабря 1895 года или равна null";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}