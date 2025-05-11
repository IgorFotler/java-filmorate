package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.*;

import static ru.yandex.practicum.filmorate.storage.InMemoryUserStorage.checkName;

@Service
@RequiredArgsConstructor
@Primary
public class UserStorageDAO implements UserStorage {

    private final UserRepository userRepository;

    @Override
    public User create(User user) {
        checkName(user);
        return userRepository.create(user);
    }

    @Override
    public List<User> getAll() {
        return userRepository.getAll();
    }

    @Override
    public User getById(Long id) {
        return userRepository.getById(id);
    }

    @Override
    public User update(User user) {
        checkName(user);
        userRepository.update(user);
        return user;
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        userRepository.addFriend(userId, friendId);
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        userRepository.removeFriend(userId, friendId);
    }

    @Override
    public List<User> getFriends(Long userId) {
        return userRepository.getFriends(userId);
    }

    @Override
    public List<User> getCommonFriends(Long firstUserId, Long secondUserId) {
        return userRepository.getCommonFriends(firstUserId, secondUserId);
    }
}
