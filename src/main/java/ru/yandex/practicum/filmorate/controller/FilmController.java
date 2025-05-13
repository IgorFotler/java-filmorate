package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @PostMapping
    public FilmDto create(@RequestBody @Valid FilmDto filmDto) {
        log.info("Получен HTTP-запрос на создание фильма: {}", filmDto);
        FilmDto createdFilm = filmService.create(filmDto);
        log.info("Успешно обработан HTTP-запрос на создание фильма: {}", filmDto);
        return createdFilm;
    }

    @GetMapping
    public List<FilmDto> getAll() {
        log.info("Получен HTTP-запрос на получение фильмов");
        List<FilmDto> allFilms = filmService.getAll();
        log.info("Успешно обработан HTTP-запрос на получение фильмов");
        return allFilms;
    }

    @GetMapping("/{id}")
    public FilmDto getById(@PathVariable Long id) {
        log.info("Получен HTTP-запрос на получение фильма по id: {}", id);
        FilmDto filmDto = filmService.getById(id);
        log.info("Найденный фильм: {}", filmDto);
        return filmDto;
    }

    @PutMapping
    public FilmDto update(@RequestBody @Valid FilmDto filmDto) {
        log.info("Получен HTTP-запрос на обновление фильма: {}", filmDto);
        filmService.update(filmDto);
        log.info("Успешно выполнен HTTP-запрос на обновление фильма: {}", filmDto);
        return filmDto;
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        log.info("Получен HTTP-запрос на удаление фильма по id: {}", id);
        filmService.deleteById(id);
        log.info("Успешно выполнен HTTP-запрос на удаление фильма с id {}", id);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@PathVariable Long filmId, @PathVariable Long userId) {
        log.info("Получен HTTP-запрос на лайк фильма: {} от пользователя: {}", filmId, userId);
        filmService.addLike(filmId, userId);
        log.info("Успешно обработан HTTP-запрос на лайк фильма: {} от пользователя: {}", filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void removeLike(@PathVariable Long filmId, @PathVariable Long userId) {
        log.info("Получен HTTP-запрос на удаление лайка фильма: {} от пользователя: {}", filmId, userId);
        filmService.removeLike(filmId, userId);
        log.info("Успешно обработан HTTP-запрос на удаление лайка фильма: {} от пользователя: {}", filmId, userId);
    }

    @GetMapping("/popular")
    public List<FilmDto> getTopFilms(@RequestParam(required = false, defaultValue = "10")Integer count) {
        log.info("Получен HTTP-запрос на получение топ-{} популярных фильмов", count);
        List<FilmDto> topFilms = filmService.getTopFilms(count);
        log.info("Успешно обработан HTTP-запрос на получение топ-{} популярных фильмов", count);
        return topFilms;
    }
}
