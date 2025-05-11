package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.mappes.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;
    private final UserMapper userMapper;

    public UserDto create(UserDto userDto) {
        User user = userMapper.convertToUser(userDto);
        userStorage.create(user);
        return userMapper.convertToUserDto(user);
    }

    public List<UserDto> getAll() {
        return userStorage.getAll().stream()
                .map(userMapper::convertToUserDto)
                .toList();
    }

    public UserDto getById(Long id) {
        User user = userStorage.getById(id);
        return userMapper.convertToUserDto(user);
    }

    public UserDto update(UserDto userDto) {
        User user = userMapper.convertToUser(userDto);
        userStorage.update(user);
        return userMapper.convertToUserDto(user);
    }

    public void deleteById(Long id) {
        userStorage.deleteById(id);
    }

    public void addFriend(Long userId, Long friendId) {
        userStorage.addFriend(userId, friendId);
    }

    public void removeFriend(Long userId, Long friendId) {
        userStorage.removeFriend(userId, friendId);
    }

    public List<UserDto> getFriends(Long userId) {
        return userStorage.getFriends(userId).stream()
                .map(userMapper::convertToUserDto)
                .toList();
    }


    public List<UserDto> getCommonFriends(Long firstUserId, Long secondUserId) {
        return userStorage.getCommonFriends(firstUserId, secondUserId).stream()
                .map(userMapper::convertToUserDto)
                .toList();
    }
}
