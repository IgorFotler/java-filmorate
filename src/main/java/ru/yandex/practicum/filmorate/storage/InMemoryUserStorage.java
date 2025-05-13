package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class InMemoryUserStorage implements UserStorage {

    private Map<Long, User> idToUser = new HashMap<>();
    private Long idCounter = 1L;

    @Override
    public User create(User user) {
        user.setId(idCounter++);
        UserStorageUtil.checkName(user);
        idToUser.put(user.getId(), user);
        return user;
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(idToUser.values());
    }

    @Override
    public User getById(Long id) {
        return idToUser.values()
                .stream()
                .filter(user -> Objects.equals(user.getId(), id))
                .findFirst()
                .orElseThrow(() -> {
                    String errorMessage = String.format("Пользователь с id %d не найден", id);
                    log.error(errorMessage);
                    throw new UserNotFoundException(errorMessage);
                });
    }

    @Override
    public User update(User user) {
        Long id = user.getId();

        if (!idToUser.containsKey(id)) {
            String errorMessage = String.format("Пользователь с id %d не найден.", id);
            log.error(errorMessage);
            throw new UserNotFoundException(errorMessage);
        }

        UserStorageUtil.checkName(user);
        idToUser.put(user.getId(), user);
        return user;
    }

    @Override
    public void deleteById(Long id) {
        if (idToUser.containsKey(id)) {
            idToUser.remove(id);
        } else {
            String errorMessage = String.format("Пользователь с id %d не найден", id);
            log.error(errorMessage);
            throw new UserNotFoundException(errorMessage);
        }
    }

    public void addFriend(Long userId, Long friendId) {
        User user = getById(userId);
        User friend = getById(friendId);

        if (userId.equals(friendId)) {
            throw new IllegalArgumentException("Введен собственный id");
        }

        user.addFriend(friendId);
        friend.addFriend(userId);

        update(user);
        update(friend);
    }

    public void removeFriend(Long userId, Long friendId) {
        User user = getById(userId);
        User friend = getById(friendId);

        if (userId.equals(friendId)) {
            throw new IllegalArgumentException("Введен собственный id");
        }

        user.removeFriend(friendId);
        friend.removeFriend(userId);

        update(user);
        update(friend);
    }


    public List<User> getFriends(Long userId) {
        User user = getById(userId);
        return user.getFriendIds().stream()
                .map(id -> getById(id))
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Long firstUserId, Long secondUserId) {
        Set<Long> firstUserFriendIds = getById(firstUserId).getFriendIds();
        Set<Long> secondUserFriendIds = getById(secondUserId).getFriendIds();
        return firstUserFriendIds.stream()
                .filter(id -> secondUserFriendIds.contains(id))
                .map(id -> getById(id))
                .collect(Collectors.toList());
    }
}
