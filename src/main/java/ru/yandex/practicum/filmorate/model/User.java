package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    private Long id;
    private String name;

    @NotBlank
    @NotEmpty(message = "Логин не должен быть null или пуст")
    private String login;

    @NotNull(message = "Поле email не должно быть null")
    @Email
    private String email;

    @NotNull(message = "Дата рождения не должна быть null")
    @PastOrPresent
    private LocalDate birthday;
}
