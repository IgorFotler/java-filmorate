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
    @Size(min = 5, max = 10, message = "Длина логина должна быть от 5 до 10 символов")
    private String login;

    @Email
    private String email;

    @PastOrPresent
    private LocalDate birthday;
}
