package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @PostMapping
    public Film create(@RequestBody @Valid Film film) {
        log.info("Получен HTTP-запрос на создание фильма: {}", film);
        filmService.create(film);
        log.info("Успешно обработан HTTP-запрос на создание фильма: {}", film);
        return film;
    }

    @GetMapping
    public List<Film> getAll() {
        log.info("Получен HTTP-запрос на получение фильмов");
        List<Film> allFilms = filmService.getAll();
        return allFilms;
    }

    @GetMapping("/{id}")
    public Film getById(Long id) {
        log.info("Получен HTTP-запрос на получение фильма по id: {}", id);
        Film film = filmService.getById(id);
        log.debug("Найденный фильм: {}", film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody @Valid Film film) {
        log.info("Получен HTTP-запрос на обновление фильма: {}", film);
        filmService.update(film);
        log.info("Успешно выполнен HTTP-запрос на обновление фильма: {}", film);
        return film;
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        log.info("Получен HTTP-запрос на удаление фильма по id: {}", id);
        filmService.deleteById(id);
        log.debug("Успешно выполнен HTTP-запрос на удаление фильма с id {}", id);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@PathVariable Long filmId, @PathVariable Long userId) {
        log.info("Получен HTTP-запрос на лайк фильма: {} от пользователя: {}", filmId, userId);
        filmService.addLike(filmId, userId);
        log.info("Успешно обработан HTTP-запрос на лайк фильма: {} от пользователя: {}", filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void removeLike(@PathVariable Long filmId, @PathVariable Long userId) {
        filmService.removeLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> getTpFilms(Integer count) {
        return filmService.getTopFilms(count);
    }
}
