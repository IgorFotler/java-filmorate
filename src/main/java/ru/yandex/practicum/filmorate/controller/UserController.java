package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.UserDto;
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
    public UserDto create(@RequestBody @Valid UserDto userDto) {
        log.info("Получен HTTP-запрос на создание пользователя: {}", userDto);
        UserDto createdUser = userService.create(userDto);
        log.info("Успешно обработан HTTP-запрос на создание пользователя: {}", userDto);
        return createdUser;
    }

    @GetMapping
    public List<UserDto> getAll() {
        log.info("Получен HTTP-запрос на получение пользователей");
        List<UserDto> allUsers = userService.getAll();
        return allUsers;
    }

    @GetMapping("/{id}")
    public UserDto getById(@PathVariable Long id) {
        log.info("Получен HTTP-запрос на получение пользователя по id: {}", id);
        UserDto userDto = userService.getById(id);
        log.debug("Найденный пользователь: {}", userDto);
        return userDto;
    }

    @PutMapping
    public UserDto update(@RequestBody @Valid UserDto userDto) {
        log.info("Получен HTTP-запрос на обновление пользователя: {}", userDto);
        userService.update(userDto);
        log.info("Успешно выполнен HTTP-запрос на обновление пользователя: {}", userDto);
        return userDto;
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        log.info("Получен HTTP-запрос на удаление пользователя по id: {}", id);
        userService.deleteById(id);
        log.info("Успешно выполнен HTTP-запрос на удаление пользователя с id {}", id);
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
    }

    @GetMapping("/{userId}/friends")
    public List<UserDto> getFriends(@PathVariable Long userId) {
        log.info("Получен HTTP-запрос на список друзей пользователя с id {}", userId);
        List<UserDto> friends = userService.getFriends(userId);
        log.info("Успешно обработан HTTP-запрос на получение списка друзей пользователя с id {}", userId);
        return friends;
    }

    @GetMapping("/{firstUserId}/friends/common/{secondUserId}")
    public List<UserDto> getCommonFriends(@PathVariable Long firstUserId, @PathVariable Long secondUserId) {
        log.info("Получен запрос на получение общих друзей у пользователей с id {} и {}", firstUserId, secondUserId);
        List<UserDto> commonFriends = userService.getCommonFriends(firstUserId, secondUserId);
        log.info("Обработан запрос на получение общих друзей у пользователей с id {} и {}", firstUserId, secondUserId);
        return commonFriends;
    }
}
