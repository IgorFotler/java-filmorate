package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.validator.NotBeforeCinemaDate;

import java.time.LocalDate;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FilmDto {
    private Long id;

    @NotEmpty(message = "Название не должно быть null или пустое")
    private String name;

    @Size(max = 200, message = "Длина описания не может превышать 200 символов")
    private String description;

    @NotBeforeCinemaDate
    private LocalDate releaseDate;

    @Positive
    private int duration;

    private Mpa mpa;

    private List<Genre> genres;
}
