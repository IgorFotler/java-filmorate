package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    private Long id;
    private String name;

    @NotEmpty(message = "Логин не должен быть null или пуст")
    @Size(min = 5, max = 10, message = "Длина логина должна быть от 5 до 10 символов")
    private String login;

    @Email
    private String email;

    @PastOrPresent
    private LocalDate birthday;
}
