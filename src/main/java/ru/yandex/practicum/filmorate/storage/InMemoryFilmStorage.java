package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Slf4j
@Service
public class InMemoryFilmStorage implements FilmStorage {

    private Map<Long, Film> idToFilm = new HashMap<>();
    private Long idCounter = 1L;

    @Override
    public Film create(Film film) {
        film.setId(idCounter++);
        idToFilm.put(film.getId(), film);
        return film;
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(idToFilm.values());
    }

    @Override
    public Film getById(Long id) {
        return idToFilm.values()
                .stream()
                .filter(film -> Objects.equals(film.getId(), id))
                .findFirst()
                .orElseThrow(() -> {
                    String errorMessage = String.format("Фильм с id %d не найден", id);
                    log.error(errorMessage);
                    throw new FilmNotFoundException(errorMessage);
                });
    }

    @Override
    public Film update(Film film) {
        Long id = film.getId();

        if (!idToFilm.containsKey(id)) {
            String errorMessage = String.format("Фильм с id %d не найден.", id);
            log.error(errorMessage);
            throw new FilmNotFoundException(errorMessage);
        }

        idToFilm.put(film.getId(), film);
        return film;
    }

    @Override
    public void deleteById(Long id) {
        if (idToFilm.containsKey(id)) {
            idToFilm.remove(id);
        } else {
            String errorMessage = String.format("Фильм с id %d не найден", id);
            log.error(errorMessage);
            throw new FilmNotFoundException(errorMessage);
        }
    }
}
