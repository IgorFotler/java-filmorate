package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final FilmMapper filmMapper;

    public FilmDto create(FilmDto filmDto) {
        Film film = filmMapper.convertToFilm(filmDto);
        filmStorage.create(film);
        return filmMapper.convertToFilmDto(film);

    }

    public List<FilmDto> getAll() {
        return filmStorage.getAll().stream()
                .map(filmMapper::convertToFilmDto)
                .toList();
    }

    public FilmDto getById(Long id) {
        Film film = filmStorage.getById(id);
        return filmMapper.convertToFilmDto(film);
    }

    public FilmDto update(FilmDto filmDto) {
        Film film = filmMapper.convertToFilm(filmDto);
        filmStorage.update(film);
        return filmMapper.convertToFilmDto(film);
    }

    public void deleteById(Long id) {
        filmStorage.deleteById(id);
    }

    public void addLike(Long filmId, Long userId) {
        filmStorage.addLike(filmId, userId);
    }

    public void removeLike(Long filmId, Long userId) {
        filmStorage.removeLike(filmId, userId);
    }

    public List<FilmDto> getTopFilms(Integer count) {
        return filmStorage.getTopFilms(count).stream()
                .map(filmMapper::convertToFilmDto)
                .toList();
    }
}
