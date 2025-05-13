package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.mapper.MpaRowMapper;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class MpaStorage {

    private final JdbcTemplate jdbcTemplate;
    private final MpaRowMapper mpaRowMapper;

    public List<Mpa> getAll() {
        return jdbcTemplate.query("SELECT * FROM MPA", mpaRowMapper);
    }

    public Optional<Mpa> getById(Integer id) {
        try {
            Mpa mpa = jdbcTemplate.queryForObject("SELECT * FROM MPA WHERE mpa_id = ?", mpaRowMapper, id);
            return Optional.ofNullable(mpa);
        } catch (EmptyResultDataAccessException ignored) {
            log.error("Рейтинг с данным id не найден: {}", id);
            return Optional.empty();
        }
    }
}