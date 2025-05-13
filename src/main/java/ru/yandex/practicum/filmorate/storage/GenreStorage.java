package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.mapper.GenreRowMapper;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class GenreStorage {

    private final JdbcTemplate jdbcTemplate;
    private final GenreRowMapper genreRowMapper;

    public List<Genre> getAll() {
        return jdbcTemplate.query("SELECT * FROM genres", genreRowMapper);
    }

    public Optional<Genre> getById(Integer id) {
        try {
            Genre genre = jdbcTemplate.queryForObject("SELECT * FROM genres WHERE id = ?", genreRowMapper, id);
            return Optional.ofNullable(genre);
        } catch (EmptyResultDataAccessException ignored) {
            log.error("Жанр с данным id не найден: {}", id);
            return Optional.empty();
        }
    }
}