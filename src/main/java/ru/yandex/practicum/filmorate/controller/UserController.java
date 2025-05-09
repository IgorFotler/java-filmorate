package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public User create(@RequestBody @Valid User user) {
        log.info("Получен HTTP-запрос на создание пользователя: {}", user);
        User createdUser = userService.create(user);
        log.info("Успешно обработан HTTP-запрос на создание пользователя: {}", user);
        return createdUser;
    }

    @GetMapping
    public List<User> getAll() {
        log.info("Получен HTTP-запрос на получение пользователей");
        List<User> allUsers = userService.getAll();
        return allUsers;
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        log.info("Получен HTTP-запрос на получение пользователя по id: {}", id);
        User user = userService.getById(id);
        log.debug("Найденный пользователь: {}", user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody @Valid User user) {
        log.info("Получен HTTP-запрос на обновление пользователя: {}", user);
        userService.update(user);
        log.info("Успешно выполнен HTTP-запрос на обновление пользователя: {}", user);
        return user;
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        log.info("Получен HTTP-запрос на удаление пользователя по id: {}", id);
        userService.deleteById(id);
        log.debug("Успешно выполнен HTTP-запрос на удаление пользователя с id {}", id);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void addFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        log.info("Получен HTTP-запрос на добавление в друзья пользователя с id {}", friendId);
        userService.addFriend(userId, friendId);
        log.info("Успешно выполнен HTTP-запрос на добавление в друзья пользователя с id {}", friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void removeFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        log.info("Получен HTTP-запрос на удаление из друзей пользователя с id {}", friendId);
        userService.removeFriend(userId, friendId);
        log.info("Успешно выполнен HTTP-запрос на удаление из друзей пользователяс с id {}", friendId);
    }

    @GetMapping("/{userId}/friends")
    public List<User> getFriends(@PathVariable Long userId) {
        log.info("Получен HTTP-запрос на список друзей пользователя с id {}", userId);
        List<User> friends = userService.getFriends(userId);
        log.info("Успешно обработан HTTP-запрос на получение списка друзей пользователя с id {}", userId);
        return friends;
    }

    @GetMapping("/{firstUserId}/friends/common/{secondUserId}")
    public List<User> getCommonFriends(@PathVariable Long firstUserId, @PathVariable Long secondUserId) {
        log.info("Получен запрос на получение общих друзей у пользователей с id {} и {}", firstUserId, secondUserId);
        List<User> commonFriends = userService.getCommonFriends(firstUserId, secondUserId);
        log.info("Обработан запрос на получение общих друзей у пользователей с id {} и {}", firstUserId, secondUserId);
        return commonFriends;
    }
}
