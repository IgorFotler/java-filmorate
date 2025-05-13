package ru.yandex.practicum.filmorate.mappers;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;

@Component
public class FilmMapper {

    public FilmDto convertToFilmDto(Film film) {
        return new FilmDto(film.getId(), film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa(), film.getGenres());
    }

    public Film convertToFilm(FilmDto filmDto) {
        return new Film(filmDto.getId(), filmDto.getName(), filmDto.getDescription(), filmDto.getReleaseDate(), filmDto.getDuration(), filmDto.getMpa(), filmDto.getGenres());
    }
}