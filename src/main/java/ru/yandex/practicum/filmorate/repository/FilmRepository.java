package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.mapper.FilmRowMapper;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class FilmRepository {
    private final JdbcTemplate jdbcTemplate;
    private final FilmRowMapper filmRowMapper;

    public Film create(Film film) {
        validationGenre(film);
        validationMpa(film);

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
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update("INSERT INTO genre_items (film_id, genre_id) " +
                        "SELECT ?, ? FROM dual " +
                        "WHERE NOT EXISTS (" +
                        "SELECT 1 FROM genre_items WHERE film_id = ? AND genre_id = ?" +
                        ")", filmId, genre.getId(), filmId, genre.getId());
            }
        }
        return film;
    }

    public List<Film> getAll() {
        String sql = "SELECT f.id AS film_id, f.name, f.description, f.release_date, f.duration, " +
                "m.mpa_id, m.name AS mpa_name, " +
                "g.id AS genre_id, g.name AS genre_name " +
                "FROM films f " +
                "LEFT JOIN mpa m ON f.mpa_id = m.mpa_id " +
                "LEFT JOIN genre_items gi ON f.id = gi.film_id " +
                "LEFT JOIN genres g ON gi.genre_id = g.id " +
                "ORDER BY f.id";

        Map<Long, Film> filmMap = new LinkedHashMap<>();

        jdbcTemplate.query(sql, rs -> {
            Long filmId = rs.getLong("film_id");

            Film film = filmMap.computeIfAbsent(filmId, id -> {
                try {
                    return Film.builder()
                            .id(id)
                            .name(rs.getString("name"))
                            .description(rs.getString("description"))
                            .releaseDate(rs.getDate("release_date").toLocalDate())
                            .duration(rs.getInt("duration"))
                            .mpa(Mpa.builder()
                                    .id(rs.getInt("mpa_id"))
                                    .name(rs.getString("name"))
                                    .build())
                            .genres(new ArrayList<>())
                            .build();
                } catch (SQLException e) {
                    throw new RuntimeException("Ошибка при маппинге фильма", e);
                }
            });

            int genreId = rs.getInt("genre_id");
            if (!rs.wasNull()) {
                Genre genre = Genre.builder()
                        .id(genreId)
                        .name(rs.getString("name"))
                        .build();
                if (!film.getGenres().contains(genre)) {
                    film.getGenres().add(genre);
                }
            }
        });

        return new ArrayList<>(filmMap.values());
    }

    public Film getById(Long id) {
        checkFilmId(id);
        return jdbcTemplate.queryForObject("SELECT * FROM films WHERE id = ?", filmRowMapper, id);
    }

    public Film update(Film film) {
        validationGenre(film);
        validationMpa(film);

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
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            jdbcTemplate.update("DELETE FROM genre_items WHERE film_id = ?", id);
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update("INSERT INTO genre_items (film_id, genre_id) VALUES (?, ?)", id, genre.getId());
            }
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
        String sql = "SELECT f.id, f.name, f.description, f.release_date, f.duration, " +
               " m.mpa_id, m.name AS mpa_name, " +
               "g.id AS genre_id, g.name AS genre_name " +
        "FROM films f " +
        "LEFT JOIN mpa m ON f.mpa_id = m.mpa_id " +
        "LEFT JOIN genre_items gi ON f.id = gi.film_id " +
        "LEFT JOIN genres g ON gi.genre_id = g.id " +
        "LEFT JOIN (SELECT film_id, COUNT(user_id) AS like_count " +
            "FROM likes " +
            "GROUP BY film_id) " +
        "l ON f.id = l.film_id " +
        "ORDER BY l.like_count DESC NULLS LAST " +
        "LIMIT ? ";

        return jdbcTemplate.query(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, count);
            return ps;
        }, rs -> {
            Map<Long, Film> filmMap = new LinkedHashMap<>();

            while (rs.next()) {
                Long filmId = rs.getLong("id");

                Film film = filmMap.computeIfAbsent(filmId, id -> {
                    try {
                        return Film.builder()
                                .id(id)
                                .name(rs.getString("name"))
                                .description(rs.getString("description"))
                                .releaseDate(rs.getDate("release_date").toLocalDate())
                                .duration(rs.getInt("duration"))
                                .mpa(Mpa.builder()
                                        .id(rs.getInt("mpa_id"))
                                        .name(rs.getString("name"))
                                        .build())
                                .genres(new ArrayList<>())
                                .build();
                    } catch (SQLException e) {
                        throw new RuntimeException("Ошибка при маппинге фильма", e);
                    }
                });

                int genreId = rs.getInt("genre_id");
                if (!rs.wasNull()) {
                    Genre genre = Genre.builder()
                            .id(genreId)
                            .name(rs.getString("name"))
                            .build();

                    if (!film.getGenres().contains(genre)) {
                        film.getGenres().add(genre);
                    }
                }
            }

            return new ArrayList<>(filmMap.values());
        });
    }

    public void checkFilmId(Long id) {
        if (jdbcTemplate.queryForObject("SELECT NOT EXISTS(SELECT 1 FROM films WHERE id = ?)", Boolean.class, id)) {
            String errorMessage = String.format("Фильм с id %d не найден.", id);
            log.error(errorMessage);
            throw new FilmNotFoundException(errorMessage);
        }
    }

    public void validationMpa(Film film) {
        if (film.getMpa() != null) {
            if (film.getMpa().getId() < 1 || film.getMpa().getId() > 5) {
                throw new FilmNotFoundException("MPA ID должен быть в диапазоне от 1 до 6");
            }
        }
    }

    public void validationGenre(Film film) {
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                if (genre.getId() < 1 || genre.getId() > 6) {
                    throw new FilmNotFoundException("Genre ID должен быть в диапазоне от 1 до 6");
                }
            }
        }
    }
}
