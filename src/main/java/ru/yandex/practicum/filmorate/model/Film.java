package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.yandex.practicum.filmorate.validator.NotBeforeCinemaDate;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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

    @Positive
    private int duration;

    @Getter
    final Set<Long> likes = new HashSet<>();

    public void addLike(Long userId) {
        likes.add(userId);
    }

    public void removeLike(Long userId) {
        likes.remove(userId);
    }
}
