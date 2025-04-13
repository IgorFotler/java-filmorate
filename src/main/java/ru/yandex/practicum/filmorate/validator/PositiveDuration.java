package ru.yandex.practicum.filmorate.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PositiveDurationValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface PositiveDuration {
    String message() default "Продолжительность должна быть положительной";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
