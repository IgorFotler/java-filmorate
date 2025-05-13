package ru.yandex.practicum.filmorate.repository.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FilmRowMapper implements RowMapper<Film> {

    private final JdbcTemplate jdbcTemplate;
    private final GenreRowMapper genreRowMapper;
    private final MpaRowMapper mpaRowMapper;

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Long filmId = rs.getLong("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
        int duration = rs.getInt("duration");
        Mpa mpa = getMpa(filmId);
        List<Genre> genres = getGenres(filmId);

        return Film.builder()
                .id(filmId)
                .name(name)
                .description(description)
                .releaseDate(releaseDate)
                .duration(duration)
                .mpa(mpa)
                .genres(genres)
                .build();
    }

    private Mpa getMpa(Long filmId) {
        return jdbcTemplate.queryForObject("SELECT * FROM MPA JOIN films ON MPA.mpa_id = films.mpa_id" +
                " WHERE films.id = ?", mpaRowMapper, filmId);
    }

    private List<Genre> getGenres(Long filmId) {
        return jdbcTemplate.query("SELECT * FROM genres JOIN genre_items ON genres.id = genre_items.genre_id" +
                " WHERE genre_items.film_id = ?", genreRowMapper, filmId);
    }
}
