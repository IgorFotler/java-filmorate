package ru.yandex.practicum.filmorate.service;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenreService {

    private final GenreStorage genreStorage;

    public List<Genre> getAll() {
        if (genreStorage.getAll().isEmpty()) {
            log.info("Ошибка валидации, список жанров пуст");
            throw new NotFoundException("Список жанров пуст");
        }
        log.info("Отправлен список жанров");
        return genreStorage.getAll().stream()
                .sorted(Comparator.comparing(Genre::getId))
                .toList();
    }

    public Genre getById(Integer id) {
        return genreStorage.getById(id)
                .orElseThrow(() -> new NotFoundException("Жанр с данным id не найден: " + id));
    }
}