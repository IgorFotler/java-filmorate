package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    Film create(Film film);

    List<Film> getAll();

    Film getById(Long id);

    Film update(Film film);

    void deleteById(Long id);

    List<Film> getTopFilms(Integer count);
}
