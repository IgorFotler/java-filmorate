package ru.yandex.practicum.filmorate.service;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Film create(Film film) {
        filmStorage.create(film);
        return film;
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film getById(Long id) {
        return filmStorage.getById(id);
    }

    public Film update(Film film) {
        filmStorage.update(film);
        return film;
    }

    public void deleteById(Long id) {
        filmStorage.deleteById(id);
    }

    public void addLike(Long filmId, Long userId) {
        Film filmById = filmStorage.getById(filmId);
        User user = userStorage.getById(userId);

        filmById.addLike(userId);
        filmStorage.update(filmById);
    }

    public void removeLike(Long filmId, Long userId) {
        Film filmById = filmStorage.getById(filmId);
        User user = userStorage.getById(userId);

        filmById.removeLike(userId);
        filmStorage.update(filmById);
    }

    public List<Film> getTopFilms(@Positive Integer count) {
        return filmStorage.getTopFilms(count);
    }
}
