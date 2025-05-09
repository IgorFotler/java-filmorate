package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public User create(User user) {
        userStorage.create(user);
        return user;
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User getById(Long id) {
        return userStorage.getById(id);
    }

    public User update(User user) {
        userStorage.update(user);
        return user;
    }

    public void deleteById(Long id) {
        userStorage.deleteById(id);
    }

    public void addFriend(Long userId, Long friendId) {
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);

        if (userId.equals(friendId)) {
            throw new IllegalArgumentException("Введен собственный id");
        }

        user.addFriend(friendId);
        friend.addFriend(userId);

        userStorage.update(user);
        userStorage.update(friend);
    }

    public void removeFriend(Long userId, Long friendId) {
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);

        if (userId.equals(friendId)) {
            throw new IllegalArgumentException("Введен собственный id");
        }

        user.removeFriend(friendId);
        friend.removeFriend(userId);

        userStorage.update(user);
        userStorage.update(friend);
    }

    public List<User> getFriends(Long userId) {
        User user = userStorage.getById(userId);
        return user.getFriendIds().stream()
                .map(id -> userStorage.getById(id))
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Long firstUserId, Long secondUserId) {
        Set<Long> firstUserFriendIds = userStorage.getById(firstUserId).getFriendIds();
        Set<Long> secondUserFriendIds = userStorage.getById(secondUserId).getFriendIds();
        return firstUserFriendIds.stream()
                .filter(id -> secondUserFriendIds.contains(id))
                .map(id -> userStorage.getById(id))
                .collect(Collectors.toList());
    }
}
