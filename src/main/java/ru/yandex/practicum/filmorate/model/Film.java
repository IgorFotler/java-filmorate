package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.yandex.practicum.filmorate.validator.NotBeforeCinemaDate;
import ru.yandex.practicum.filmorate.validator.PositiveDuration;

import java.time.Duration;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Film {
    private Long id;

    @NotEmpty(message = "Название не должно быть null или пустое")
    private String name;

    @Size(max = 200, message = "Длина описания не может превышать 200 символов")
    private String description;

    @NotBeforeCinemaDate
    private LocalDate releaseDate;

    @PositiveDuration
    private Duration duration;
}
