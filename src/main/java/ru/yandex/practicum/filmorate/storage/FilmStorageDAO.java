package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Primary
public class FilmStorageDAO implements FilmStorage {

    private final FilmRepository filmRepository;

    @Override
    public Film create(Film film) {
        return filmRepository.create(film);
    }

    @Override
    public List<Film> getAll() {
        return filmRepository.getAll();
    }

    @Override
    public Film getById(Long id) {
        return filmRepository.getById(id);
    }

    @Override
    public Film update(Film film) {
        return filmRepository.update(film);
    }

    @Override
    public void deleteById(Long id) {
        filmRepository.deleteById(id);
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        filmRepository.addLike(filmId, userId);
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        filmRepository.removeLike(filmId, userId);
    }

    @Override
    public List<Film> getTopFilms(Integer count) {
        return filmRepository.getTopFilms(count);
    }
}
