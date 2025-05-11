package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.mapper.FilmRowMapper;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class FilmRepository {
    private final JdbcTemplate jdbcTemplate;
    private final FilmRowMapper filmRowMapper;

    public Film create(Film film) {

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement("INSERT INTO films (name, description, release_date, duration, mpa_id) VALUES (?, ?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS);

            ps.setObject(1, film.getName());
            ps.setObject(2, film.getDescription());
            ps.setObject(3, film.getReleaseDate());
            ps.setObject(4, film.getDuration());
            ps.setObject(5, film.getMpa().getId());

            return ps;
        }, keyHolder);

        Long filmId = keyHolder.getKeyAs(Long.class);

        if (filmId != null) {
            film.setId(filmId);
        } else {
            throw new FilmNotFoundException("Не удалось сохранить данные");
        }

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            //jdbcTemplate.update("DELETE FROM genre_items WHERE film_id = ?", filmId); --нужно будет вставить при обновлении
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update("INSERT INTO genre_items (film_id, genre_id) VALUES (?, ?)", filmId, genre.getId());
            }
        }
        return film;
    }

    public List<Film> getAll() {
        String query = "SELECT * FROM films;";
        return jdbcTemplate.query(query, filmRowMapper);
    }

    public Film getById(Long id) {
        checkFilmId(id);
        return jdbcTemplate.queryForObject("SELECT * FROM films WHERE id = ?", filmRowMapper, id);
    }

    public Film update(Film film) {
        Long id = film.getId();
        checkFilmId(id);
        jdbcTemplate.update(
                "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? WHERE id = ?",
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                id
        );
        jdbcTemplate.update("DELETE FROM genre_items WHERE film_id = ?", id);
        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update("INSERT INTO genre_items (film_id, genre_id) VALUES (?, ?)", id, genre.getId());
        }
        return film;
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM films WHERE id = ?", id);
    }

    public void addLike(Long filmId, Long userId) {
        jdbcTemplate.update("INSERT INTO likes(film_id, user_id) VALUES(?,?)", filmId, userId);
    }

    public void removeLike(Long filmId, Long userId) {
        jdbcTemplate.update("DELETE FROM likes WHERE film_id = ? AND user_id = ?", filmId, userId);
    }

    public List<Film> getTopFilms(Integer count) {
        return jdbcTemplate.query("SELECT F.* FROM films AS F JOIN (SELECT film_id, " +
                "COUNT(user_id) AS like_count FROM likes GROUP BY film_id) AS L " +
                "ON F.id = L.film_id " +
                "ORDER BY L.like_count DESC LIMIT ?", filmRowMapper, count);
    }

    public void checkFilmId(Long id) {
        if (jdbcTemplate.queryForObject("SELECT NOT EXISTS(SELECT 1 FROM films WHERE id = ?)", Boolean.class, id)) {
            String errorMessage = String.format("Фильм с id %d не найден.", id);
            log.error(errorMessage);
            throw new FilmNotFoundException(errorMessage);
        }
    }
}
