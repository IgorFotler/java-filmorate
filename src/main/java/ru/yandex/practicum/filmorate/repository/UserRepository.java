package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.mapper.UserRowMapper;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper userRowMapper;
    String CREATE_QUERY = "INSERT INTO users (name, login, email, birthday) values (?, ?, ?, ?);";

    public User create(User user) {

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(CREATE_QUERY, Statement.RETURN_GENERATED_KEYS);

            ps.setObject(1, user.getName());
            ps.setObject(2, user.getLogin());
            ps.setObject(3, user.getEmail());
            ps.setObject(4, user.getBirthday());

            return ps;
        }, keyHolder);

        Long id = keyHolder.getKeyAs(Long.class);

        if (id != null) {
            user.setId(id);
        } else {
            throw new UserNotFoundException("Не удалось сохранить данные");
        }
        return user;
    }

    public List<User> getAll() {
        String query = "SELECT * FROM users;";
        return jdbcTemplate.query(query, userRowMapper);
    }

    public User getById(Long id) {
        if (jdbcTemplate.queryForObject("SELECT NOT EXISTS(SELECT 1 FROM users WHERE id = ?)", Boolean.class, id)) {
            String errorMessage = String.format("Пользователь с id %d не найден.", id);
            log.error(errorMessage);
            throw new UserNotFoundException(errorMessage);
        }
        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE id = ?", userRowMapper, id);
    }

    public User update(User user) {
        Long id = user.getId();

        if (jdbcTemplate.queryForObject("SELECT NOT EXISTS(SELECT 1 FROM users WHERE id = ?)", Boolean.class, id)) {
            String errorMessage = String.format("Пользователь с id %d не найден.", id);
            log.error(errorMessage);
            throw new UserNotFoundException(errorMessage);
        }

        jdbcTemplate.update(
                "UPDATE users SET name = ?, login = ?, email = ?, birthday = ? WHERE id = ?",
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                user.getBirthday(),
                id
        );
        return user;
    }

    public void deleteById(Long id) {
        if (jdbcTemplate.queryForObject("SELECT NOT EXISTS(SELECT 1 FROM users WHERE id = ?)", Boolean.class, id)) {
            String errorMessage = String.format("Пользователь с id %d не найден.", id);
            log.error(errorMessage);
            throw new UserNotFoundException(errorMessage);
        }
        jdbcTemplate.update("DELETE FROM users WHERE id = ?", id);
    }

    public void addFriend(Long userId, Long friendId) {
        if (userId.equals(friendId)) {
            throw new IllegalArgumentException("Введен собственный id");
        }

        jdbcTemplate.update("INSERT INTO friends (user_id, friend_id) VALUES(?,?)", userId, friendId);
    }

    public void removeFriend(Long userId, Long friendId) {
        int rowsDeleted = jdbcTemplate.update("DELETE FROM friends WHERE user_id = ? AND friend_id = ?", userId, friendId);
        if (rowsDeleted == 0) {
            log.info("Не удалось удалить записи из таблицы Friends");
        } else {
            log.info("Удалено {} строк(и) из таблицы Friends", rowsDeleted);
        }
    }

    public List<User> getFriends(Long userId) {
        List<Integer> friendsId = jdbcTemplate.queryForList("SELECT friend_id FROM friends WHERE user_id = ?", new Object[]{userId}, Integer.class);
        List<User> friends = new ArrayList<>();
        for (Integer id : friendsId) {
            friends.add(jdbcTemplate.queryForObject("SELECT * FROM users WHERE id = ?", userRowMapper, id));
        }
        return friends;
    }

    public List<User> getCommonFriends(Long firstUserId, Long secondUserId) {
        if (firstUserId.equals(secondUserId)) {
            throw new IllegalArgumentException("Введен собственный id");
        }

        List<Integer> userFriends = jdbcTemplate.queryForList("SELECT friend_id FROM friends WHERE user_id = ?", new Object[]{firstUserId}, Integer.class);
        List<Integer> otherUserFriends = jdbcTemplate.queryForList("SELECT friend_id FROM friends WHERE user_id = ?", new Object[]{secondUserId}, Integer.class);

        userFriends.retainAll(otherUserFriends);

        if (userFriends.isEmpty()) {
            throw new UserNotFoundException("У пользователей нет общих друзей.");
        }

        List<User> commonFriends = new ArrayList<>();
        for (Integer friendId : userFriends) {
            commonFriends.add(jdbcTemplate.queryForObject("SELECT * FROM users WHERE id = ?", userRowMapper, friendId));
        }

        return commonFriends;
    }
}


