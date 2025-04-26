package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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

    @Getter
    final Set<Long> friendIds = new HashSet<>();

    public void addFriend(Long friendId) {
        friendIds.add(friendId);
    }

    public void removeFriend(Long friendId) {
        friendIds.remove(friendId);
    }
}
