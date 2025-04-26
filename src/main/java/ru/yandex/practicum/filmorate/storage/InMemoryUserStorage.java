package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Service
public class InMemoryUserStorage implements UserStorage {

    private Map<Long, User> idToUser = new HashMap<>();
    private Long idCounter = 1L;

    @Override
    public User create(User user) {
        user.setId(idCounter++);
        checkName(user);
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

        checkName(user);
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

    private void checkName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}

